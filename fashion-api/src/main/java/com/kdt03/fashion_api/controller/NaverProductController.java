package com.kdt03.fashion_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt03.fashion_api.domain.dto.NaverProductDTO;
import com.kdt03.fashion_api.service.NaverProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver-products")
public class NaverProductController {

    private final NaverProductService naverProductService;

    @GetMapping("/list")
    public ResponseEntity<List<NaverProductDTO>> getAllNaverProducts() {
        return ResponseEntity.ok(naverProductService.getAllNaverProducts());
    }
}
