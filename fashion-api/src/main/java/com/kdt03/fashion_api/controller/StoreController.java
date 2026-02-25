package com.kdt03.fashion_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt03.fashion_api.domain.dto.StoreDTO;
import com.kdt03.fashion_api.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "매장 정보 (Store)", description = "매장 목록 조회 관련 API")
@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "전체 매장 목록 조회", description = "등록된 모든 매장 목록을 반환합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "[\n  {\n    \"storeId\": \"online\",\n    \"storeName\": \"온라인 통합 센터\",\n    \"location\": \"서울시 강남구...\"\n  },\n  {\n    \"storeId\": \"gangnam_01\",\n    \"storeName\": \"9온스 강남점\",\n    \"location\": \"서울시 강남구 역삼동...\"\n  }\n]")))
    @GetMapping("/list")
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }
}
