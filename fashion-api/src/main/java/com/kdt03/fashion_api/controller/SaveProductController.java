package com.kdt03.fashion_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kdt03.fashion_api.domain.dto.SaveProductRequestDTO;
import com.kdt03.fashion_api.domain.dto.SaveProductResponseDTO;
import com.kdt03.fashion_api.service.SaveProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/save-products")
@RequiredArgsConstructor
@Tag(name = "관심 상품 (SaveProducts)", description = "관심 상품 등록, 조회, 삭제 API")
public class SaveProductController {

    private final SaveProductService saveProductService;

    @PostMapping
    @Operation(summary = "관심 상품 등록", description = "네이버 상품을 관심 상품으로 등록합니다.")
    public ResponseEntity<?> addSaveProduct(
            @RequestParam String memberId,
            @RequestBody SaveProductRequestDTO dto) {
        saveProductService.addSaveProduct(memberId, dto);
        return ResponseEntity.ok().body("관심 상품에 추가되었습니다.");
    }

    @GetMapping
    @Operation(summary = "관심 상품 목록 조회", description = "특정 회원의 관심 상품 목록을 조회합니다.")
    public ResponseEntity<List<SaveProductResponseDTO>> getMySaveProducts(
            @RequestParam String memberId) {
        return ResponseEntity.ok(saveProductService.getMySaveProducts(memberId));
    }

    @DeleteMapping("/{saveId}")
    @Operation(summary = "관심 상품 삭제", description = "관심 상품을 삭제합니다.")
    public ResponseEntity<?> deleteSaveProduct(
            @PathVariable Long saveId,
            @RequestParam String memberId) {
        saveProductService.deleteSaveProduct(saveId, memberId);
        return ResponseEntity.ok().body("관심 상품이 삭제되었습니다.");
    }
}