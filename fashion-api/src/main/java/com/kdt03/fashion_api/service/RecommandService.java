package com.kdt03.fashion_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kdt03.fashion_api.domain.NineounceProductVectors512;
import com.kdt03.fashion_api.domain.NineounceProductVectors768;
import com.kdt03.fashion_api.domain.dto.Internal768AnalysisDTO;
import com.kdt03.fashion_api.domain.dto.Internal768RecommendationResponseDTO;
import com.kdt03.fashion_api.domain.dto.RecommendationResponseDTO;
import com.kdt03.fashion_api.domain.dto.SimilarProductDTO;
import com.kdt03.fashion_api.repository.NineounceProductVectors512Repository;
import com.kdt03.fashion_api.repository.NineounceProductVectors768Repository;
import com.kdt03.fashion_api.repository.RecommandRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RecommandService {
        private final RecommandRepository recRepo;
        private final com.kdt03.fashion_api.client.InternalFastApiClient internalFastApiClient;
        private final NineounceProductVectors512Repository vectors512Repository;
        private final NineounceProductVectors768Repository vectors768Repository;

        public RecommandService(RecommandRepository recRepo,
                        com.kdt03.fashion_api.client.InternalFastApiClient internalFastApiClient,
                        NineounceProductVectors512Repository vectors512Repository,
                        NineounceProductVectors768Repository vectors768Repository) {
                this.recRepo = recRepo;
                this.internalFastApiClient = internalFastApiClient;
                this.vectors512Repository = vectors512Repository;
                this.vectors768Repository = vectors768Repository;
        }

        @Transactional(readOnly = true)
        public RecommendationResponseDTO recommand(String productId) {
                log.info("Finding similar products for Nineounce product: {}", productId);

                // 1. 네이버 유사 상품 검색
                List<SimilarProductDTO> naverResults = recRepo.findSimilarProducts(productId).stream()
                                .map(p -> new SimilarProductDTO(
                                                p.getProductId(),
                                                p.getTitle(),
                                                p.getPrice(),
                                                p.getImageUrl(),
                                                p.getProductLink(),
                                                p.getSimilarityScore()))
                                .toList();

                // 2. 내부 유사 상품 검색
                List<SimilarProductDTO> internalResults = recRepo.findSimilarInternalProducts(productId).stream()
                                .map(p -> new SimilarProductDTO(
                                                p.getProductId(),
                                                p.getTitle(),
                                                p.getPrice(),
                                                p.getImageUrl(),
                                                p.getProductLink(),
                                                p.getSimilarityScore()))
                                .toList();

                // 3. 기준 상품 스타일 상세 정보 페칭 (512차원)
                NineounceProductVectors512 v512 = vectors512Repository.findById(productId).orElse(null);

                log.info("Found {} naver and {} internal similar products.", naverResults.size(),
                                internalResults.size());

                if (!naverResults.isEmpty() || !internalResults.isEmpty()) {
                        log.info("최상위 네이버 추천: {}, 최상위 내부 추천: {}",
                                        naverResults.isEmpty() ? "없음" : naverResults.getFirst().getTitle(),
                                        internalResults.isEmpty() ? "없음" : internalResults.getFirst().getTitle());
                }

                return RecommendationResponseDTO.builder()
                                .naverProducts(naverResults)
                                .internalProducts(internalResults)
                                .targetTop1Style(v512 != null && v512.getTop1Style() != null
                                                ? v512.getTop1Style().getStyleName()
                                                : null)
                                .targetTop1Score(v512 != null ? v512.getTop1Score() : null)
                                .targetTop2Style(v512 != null && v512.getTop2Style() != null
                                                ? v512.getTop2Style().getStyleName()
                                                : null)
                                .targetTop2Score(v512 != null ? v512.getTop2Score() : null)
                                .targetTop3Style(v512 != null && v512.getTop3Style() != null
                                                ? v512.getTop3Style().getStyleName()
                                                : null)
                                .targetTop3Score(v512 != null ? v512.getTop3Score() : null)
                                .build();
        }

        @Transactional(readOnly = true)
        public RecommendationResponseDTO recommand768(String productId) {
                log.info("Finding 768-dim similar products for Nineounce product: {}", productId);

                // 1. 네이버 유사 상품 검색 (768차원)
                List<SimilarProductDTO> naverResults = recRepo.findSimilar768Products(productId).stream()
                                .map(p -> new SimilarProductDTO(
                                                p.getProductId(),
                                                p.getTitle(),
                                                p.getPrice(),
                                                p.getImageUrl(),
                                                p.getProductLink(),
                                                p.getSimilarityScore()))
                                .toList();

                // 2. 내부 유사 상품 검색 (768차원)
                List<SimilarProductDTO> internalResults = recRepo.findSimilarInternal768Products(productId).stream()
                                .map(p -> new SimilarProductDTO(
                                                p.getProductId(),
                                                p.getTitle(),
                                                p.getPrice(),
                                                p.getImageUrl(),
                                                p.getProductLink(),
                                                p.getSimilarityScore()))
                                .toList();

                // 3. 기준 상품 스타일 상세 정보 페칭 (768차원)
                NineounceProductVectors768 v768 = vectors768Repository.findById(productId).orElse(null);

                log.info("Found {} naver and {} internal similar products (768-dim).", naverResults.size(),
                                internalResults.size());

                return RecommendationResponseDTO.builder()
                                .naverProducts(naverResults)
                                .internalProducts(internalResults)
                                .targetTop1Style(v768 != null && v768.getStyleTop1() != null
                                                ? v768.getStyleTop1().getStyleName()
                                                : null)
                                .targetTop1Score(v768 != null ? v768.getStyleScore1() : null)
                                .targetTop2Style(v768 != null && v768.getStyleTop2() != null
                                                ? v768.getStyleTop2().getStyleName()
                                                : null)
                                .targetTop2Score(v768 != null ? v768.getStyleScore2() : null)
                                .targetTop3Style(v768 != null && v768.getStyleTop3() != null
                                                ? v768.getStyleTop3().getStyleName()
                                                : null)
                                .targetTop3Score(v768 != null ? v768.getStyleScore3() : null)
                                .build();
        }

        @Cacheable(value = "recommendations", key = "#file.originalFilename + #file.size", condition = "#file != null")
        public Internal768RecommendationResponseDTO analyzeInternal768(MultipartFile file) {
                log.info("Processing 768 analysis for file: {}", file.getOriginalFilename());

                try {
                        Internal768AnalysisDTO fastApiResponse = internalFastApiClient
                                        .analyzeInternal768(file.getResource());

                        if (fastApiResponse != null && fastApiResponse.getEmbedding() != null) {
                                List<Double> embeddingList = fastApiResponse.getEmbedding();
                                String vectorString = embeddingList.stream()
                                                .map(String::valueOf)
                                                .collect(Collectors.joining(",", "[", "]"));

                                java.util.concurrent.CompletableFuture<List<SimilarProductDTO>> internalTask = java.util.concurrent.CompletableFuture
                                                .supplyAsync(() -> recRepo
                                                                .findTopSimilarInternal768Products(vectorString)
                                                                .stream()
                                                                .map(p -> new SimilarProductDTO(
                                                                                p.getProductId(),
                                                                                p.getTitle(),
                                                                                p.getPrice(),
                                                                                p.getImageUrl(),
                                                                                p.getProductLink(),
                                                                                p.getSimilarityScore()))
                                                                .collect(Collectors.toList()));

                                java.util.concurrent.CompletableFuture<List<SimilarProductDTO>> naverTask = java.util.concurrent.CompletableFuture
                                                .supplyAsync(() -> recRepo.findTopSimilarNaver768Products(vectorString)
                                                                .stream()
                                                                .map(p -> new SimilarProductDTO(
                                                                                p.getProductId(),
                                                                                p.getTitle(),
                                                                                p.getPrice(),
                                                                                p.getImageUrl(),
                                                                                p.getProductLink(),
                                                                                p.getSimilarityScore()))
                                                                .collect(Collectors.toList()));

                                java.util.concurrent.CompletableFuture.allOf(internalTask, naverTask).join();
                                List<SimilarProductDTO> internalProducts = internalTask.get();
                                List<SimilarProductDTO> naverProducts = naverTask.get();

                                return Internal768RecommendationResponseDTO.builder()
                                                .dimension(fastApiResponse.getDimension())
                                                .styles(fastApiResponse.getStyles())
                                                .internalProducts(internalProducts)
                                                .naverProducts(naverProducts)
                                                .build();
                        }
                } catch (Exception e) {
                        log.error("Error during internal 768 analysis: {}", e.getMessage(), e);
                        throw new RuntimeException("768 분석 중 오류 발생: " + e.getMessage());
                }
                return null;
        }
}
