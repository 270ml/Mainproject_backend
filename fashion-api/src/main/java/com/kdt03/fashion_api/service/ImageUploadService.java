package com.kdt03.fashion_api.service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.kdt03.fashion_api.domain.Member;
import com.kdt03.fashion_api.domain.dto.AnalysisResponseDTO;
import com.kdt03.fashion_api.domain.dto.FastApiAnalysisDTO;
import com.kdt03.fashion_api.domain.dto.SimilarProductDTO;
import com.kdt03.fashion_api.repository.MemberRepository;
import com.kdt03.fashion_api.repository.NaverProductRepository;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Service
public class ImageUploadService {
    private final WebClient webClient;
    private final MemberRepository memberRepo;
    private final NaverProductRepository naverProductRepo;

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_KEY}")
    private String supabaseKey;

    @Value("${app.supabase.bucket.upload}")
    private String uploadBucket;

    @Value("${app.supabase.bucket.profile}")
    private String profileBucket;

    public ImageUploadService(WebClient.Builder webClientBuilder, MemberRepository memberRepo,
            NaverProductRepository naverProductRepo,
            @Value("${app.fastapi.url}") String fastApiUrl) {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 연결 타임아웃 5초
                .responseTimeout(Duration.ofSeconds(120)) // 전체 응답 타임아웃 120초 (분석 대기 고려)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(120, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(120, TimeUnit.SECONDS)));

        this.webClient = webClientBuilder
                .baseUrl(fastApiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        this.memberRepo = memberRepo;
        this.naverProductRepo = naverProductRepo;
    }

    @Transactional
    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String savedFilename = UUID.randomUUID().toString() + extension;

        String uploadUrl = supabaseUrl + "/storage/v1/object/" + uploadBucket + "/" + savedFilename;
        String publicUrl = supabaseUrl + "/storage/v1/object/public/" + uploadBucket + "/" + savedFilename;

        // 1. Supabase 업로드 태스크 (비동기)
        CompletableFuture<Void> supabaseTask = CompletableFuture.runAsync(() -> {
            try {
                log.info("Starting Supabase upload for {}", savedFilename);
                webClient.post()
                        .uri(uploadUrl)
                        .header("Authorization", "Bearer " + supabaseKey)
                        .header("apikey", supabaseKey)
                        .contentType(MediaType.parseMediaType(file.getContentType()))
                        .body(BodyInserters.fromResource(file.getResource()))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                log.info("Supabase upload completed for {}", savedFilename);
            } catch (Exception e) {
                log.error("Supabase upload task failed: {}", e.getMessage());
                throw new RuntimeException("Supabase 업로드 중 오류 발생", e);
            }
        });

        // 2. FastAPI 업로드 및 분석 태스크 (비동기)
        CompletableFuture<Map<String, Object>> fastApiTask = CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Starting FastAPI upload for {}", savedFilename);
                MultipartBodyBuilder builder = new MultipartBodyBuilder();
                builder.part("file", file.getResource());

                Map<String, Object> response = webClient.post()
                        .uri("/upload-image")
                        .body(BodyInserters.fromMultipartData(builder.build()))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                        .block();

                log.info("FastAPI upload completed for {}", savedFilename);
                return response != null ? response : new HashMap<>();
            } catch (Exception e) {
                log.warn("FastAPI task failed (ignoring): {}", e.getMessage());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "FastAPI 서버 연결 불가 (무시됨)");
                errorResponse.put("status", "disconnected");
                return errorResponse;
            }
        });

        // 3. 두 태스크 병렬 실행 대기
        try {
            CompletableFuture.allOf(supabaseTask, fastApiTask).join();
            Map<String, Object> fastApiResponseMap = fastApiTask.get();

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("imageUrl", publicUrl);
            result.put("savedPath", publicUrl);
            result.put("fastApiResult", fastApiResponseMap);
            return result;
        } catch (Exception e) {
            log.error("Parallel execution in uploadImage failed: {}", e.getMessage());
            throw new IOException("파일 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Transactional
    public String uploadProfileImage(MultipartFile file, String id) throws IOException {
        String savedFilename = id;
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + profileBucket + "/" + savedFilename;

        try {
            webClient.post()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .header("apikey", supabaseKey)
                    .header("x-upsert", "true")
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(BodyInserters.fromResource(file.getResource()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            String publicUrl = supabaseUrl + "/storage/v1/object/public/" + profileBucket + "/" + savedFilename;

            Member member = memberRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("없는 회원입니다."));

            member.setProfile(publicUrl);

            return publicUrl;
        } catch (Exception e) {
            log.error("Profile image upload to Supabase failed for user {}: {}", id, e.getMessage());
            throw new IOException("Supabase 업로드 실패: " + e.getMessage());
        }
    }

    public AnalysisResponseDTO uploadAndAnalyze(MultipartFile file) throws IOException {
        log.info("Processing uploadAndAnalyze for file: {}", file.getOriginalFilename());

        FastApiAnalysisDTO fastApiResponse = null;
        List<SimilarProductDTO> similarProducts = new ArrayList<>();

        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", file.getResource());

            fastApiResponse = webClient.post()
                    .uri("/embed")
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(FastApiAnalysisDTO.class)
                    .block();

            log.debug("FastAPI response: {}", fastApiResponse);

            if (fastApiResponse != null) {
                List<Double> embeddingList = fastApiResponse.getEmbedding();

                if (embeddingList == null && fastApiResponse.getAnalysisResult() != null) {
                    embeddingList = (List<Double>) fastApiResponse.getAnalysisResult().get("embedding");
                }

                if (embeddingList != null && !embeddingList.isEmpty()) {
                    String vectorString = embeddingList.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",", "[", "]"));

                    log.info("Performing vector search with vector length: {}", vectorString.length());

                    similarProducts = naverProductRepo.findTopSimilarProducts(vectorString).stream()
                            .map(p -> new SimilarProductDTO(
                                    p.getProductId(),
                                    p.getTitle(),
                                    p.getPrice(),
                                    p.getImageUrl(),
                                    p.getProductLink(),
                                    p.getSimilarityScore()))
                            .collect(Collectors.toList());

                    log.info("Found {} similar products.", similarProducts.size());
                } else {
                    log.warn("No embedding found in FastAPI response.");
                }
            }
        } catch (Exception e) {
            log.error("Error during uploadAndAnalyze: {}", e.getMessage(), e);
            if (fastApiResponse == null) {
                fastApiResponse = FastApiAnalysisDTO.builder().error(e.getMessage()).build();
            } else {
                fastApiResponse.setError(e.getMessage());
            }
        }

        Map<String, Object> finalAnalysisResult = (fastApiResponse != null
                && fastApiResponse.getAnalysisResult() != null)
                        ? fastApiResponse.getAnalysisResult()
                        : (fastApiResponse != null ? Map.of("error", fastApiResponse.getError()) : new HashMap<>());

        AnalysisResponseDTO response = AnalysisResponseDTO.builder()
                .analysisResult(finalAnalysisResult)
                .similarProducts(similarProducts)
                .build();

        log.info("Final response similarProducts count: {}",
                (response.getSimilarProducts() != null ? response.getSimilarProducts().size() : 0));
        return response;
    }
}
