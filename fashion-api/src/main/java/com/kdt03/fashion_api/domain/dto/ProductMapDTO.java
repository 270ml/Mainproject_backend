package com.kdt03.fashion_api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductMapDTO {
    private String productId;
    private String productName;
    private String style;
    private float xCoord;
    private float yCoord;
}
