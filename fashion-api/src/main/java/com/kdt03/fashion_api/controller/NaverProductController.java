package com.kdt03.fashion_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt03.fashion_api.domain.dto.NaverProductDTO;
import com.kdt03.fashion_api.service.NaverProductService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "외부 상품 관리 (Naver Products)", description = "네이버 크롤링 데이터 기반 상품 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver-products")
public class NaverProductController {

    private final NaverProductService naverProductService;

    @Operation(summary = "네이버 상품 전체 조회", description = "네이버에서 크롤링한 모든 상품 데이터를 리스트 형식으로 반환합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "[\n  {\n    \"productId\": \"50810757913\",\n    \"title\": \"남자 오버핏 후드티\",\n    \"price\": 29800,\n    \"imageUrl\": \"https://...\",\n    \"productLink\": \"https://...\"\n  }\n]")))
    @GetMapping("/list")
    public ResponseEntity<List<NaverProductDTO>> getAllNaverProducts() {
        return ResponseEntity.ok(naverProductService.getAllNaverProducts());
    }
}
