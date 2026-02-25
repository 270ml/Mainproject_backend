package com.kdt03.fashion_api.domain.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveProductResponseDTO {
    private Long saveId;
    private String naverProductId;
    private String title;
    private Integer price;
    private String imageUrl;
    private String productLink;
    private OffsetDateTime createdAt;
}
