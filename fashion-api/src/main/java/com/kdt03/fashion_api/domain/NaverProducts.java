package com.kdt03.fashion_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "naver_products")
public class NaverProducts {

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(columnDefinition = "TEXT")
    private String title;

    private Integer price;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "product_link", columnDefinition = "TEXT")
    private String productLink;

    @Column(name = "category_id")
    private String categoryId;

    @Column(columnDefinition = "TEXT")
    private String style;

}
