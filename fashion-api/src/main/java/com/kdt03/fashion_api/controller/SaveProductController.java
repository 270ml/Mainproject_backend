package com.kdt03.fashion_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt03.fashion_api.domain.dto.SaveProductRequestDTO;
import com.kdt03.fashion_api.service.SaveProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/save-products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "관심 상품 (SaveProducts)", description = "관심 상품 등록, 조회, 삭제 API")
public class SaveProductController {
    private final SaveProductService saveProductService;

    @PostMapping
    @Operation(summary = "관심 상품 등록", description = "네이버 상품을 관심 상품으로 등록합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "등록 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/plain", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "관심 상품에 추가되었습니다."))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 등록된 상품")
    })
    public ResponseEntity<?> addSaveProduct(
            @RequestBody SaveProductRequestDTO dto,
            java.security.Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        saveProductService.addSaveProduct(principal.getName(), dto);
        return ResponseEntity.ok().body("관심 상품에 추가되었습니다.");
    }

    @GetMapping
    @Operation(summary = "관심 상품 목록 조회", description = "현재 로그인한 회원의 관심 상품 목록을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "[{\"saveId\":1,\"naverProductId\":\"50810757913\",\"title\":\"남자 오버핏 후드티\",\"price\":29800,\"createdAt\":\"2024-03-21T15:30:00\"}]")))
    public ResponseEntity<?> getMySaveProducts(java.security.Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        return ResponseEntity.ok(saveProductService.getMySaveProducts(principal.getName()));
    }

    @DeleteMapping
    @Operation(summary = "관심 상품 다건 삭제", description = "naverProductId 배열을 Body로 전송하면 JWT 토큰의 회원 ID와 일치하는 항목을 DB에서 삭제합니다.\n\n**요청 예시:** `[\"50810757913\", \"58714241076\"]`")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/plain", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "선택한 관심 상품이 삭제되었습니다.")))
    public ResponseEntity<?> deleteSaveProducts(
            @RequestBody java.util.List<String> naverProductIds,
            java.security.Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        saveProductService.deleteSaveProducts(naverProductIds, principal.getName());
        return ResponseEntity.ok().body("선택한 관심 상품이 삭제되었습니다.");
    }
}