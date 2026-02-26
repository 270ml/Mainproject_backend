package com.kdt03.fashion_api.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class RecommendationResponseDTO {
    private List<SimilarProductDTO> naverProducts;
    private List<SimilarProductDTO> internalProducts;

    // 기준 상품 스타일 분석 정보
    private String targetTop1Style;
    private Double targetTop1Score;
    private String targetTop2Style;
    private Double targetTop2Score;
    private String targetTop3Style;
    private Double targetTop3Score;
}
