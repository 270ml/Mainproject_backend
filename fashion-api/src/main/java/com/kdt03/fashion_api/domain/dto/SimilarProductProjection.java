package com.kdt03.fashion_api.domain.dto;

public interface SimilarProductProjection {
    String getProductId();

    String getTitle();

    Integer getPrice();

    String getImageUrl();

    String getProductLink();

    Double getSimilarityScore();
}
