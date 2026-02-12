package com.kdt03.fashion_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdt03.fashion_api.domain.dto.YearlyTrendDTO;
import com.kdt03.fashion_api.service.TrendService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/trends")
@RequiredArgsConstructor // 서비스 주입을 위한 생성자 자동 생성
public class TrendController {
    private final TrendService trendService;

    @GetMapping("/shopping-insight")
    public List<Map<String, Object>> getStylesTrend() {
        return trendService.getIntegratedTrend();
    }

    @GetMapping("/by-year")
    public ResponseEntity<?> getSalesTrends(@RequestParam(value = "year", required = false) Integer year) {
        if(year == null) {
            return ResponseEntity.ok(trendService.getAllTrend());
        }
        return ResponseEntity.ok(trendService.getTrendByYear(year));
    }
}