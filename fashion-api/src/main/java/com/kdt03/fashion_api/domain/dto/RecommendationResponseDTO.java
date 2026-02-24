package com.kdt03.fashion_api.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendationResponseDTO {
    private List<SimilarProductDTO> naverProducts;
    private List<SimilarProductDTO> internalProducts;
}
