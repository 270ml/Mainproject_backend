package com.kdt03.fashion_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt03.fashion_api.domain.dto.SalesDTO;
import com.kdt03.fashion_api.service.SalesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    @GetMapping("/rank")
    public ResponseEntity<List<SalesDTO>> getTop10BestSellingProducts() {
        List<SalesDTO> top10Products = salesService.getTop10BestSellingProducts();
        return ResponseEntity.ok(top10Products);
    }
}
