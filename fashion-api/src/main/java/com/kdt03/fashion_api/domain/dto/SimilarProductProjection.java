package com.kdt03.fashion_api.domain.dto;

public interface SimilarProductProjection {
    String getProductId();

    String getTitle();

    String getPrice();

    String getImageUrl();

    String getProductLink();

    Double getSimilarityScore();
}
