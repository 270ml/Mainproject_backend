package com.kdt03.fashion_api.domain.dto;

import com.kdt03.fashion_api.domain.Sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDTO {
    private String productId;
    private String productName;
    private Integer saleQuantity;
    private Integer salePrice;
    private Float saleRate;

    public static SalesDTO fromEntity(Sales entity) {
        return SalesDTO.builder()
                .productId(entity.getProduct().getProductId())
                .productName(entity.getProduct().getProductName())
                .saleQuantity(entity.getSaleQuantity())
                .salePrice(entity.getSalePrice())
                .saleRate(entity.getSaleRate())
                .build();
    }
}
