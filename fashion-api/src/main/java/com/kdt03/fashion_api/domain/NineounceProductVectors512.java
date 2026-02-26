package com.kdt03.fashion_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nineounce_product_vectors_512")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NineounceProductVectors512 {

    @Id
    @Column(name = "product_id")
    private String productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top1_style")
    private Styles top1Style;

    @Column(name = "top1_score")
    private Double top1Score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top2_style")
    private Styles top2Style;

    @Column(name = "top2_score")
    private Double top2Score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top3_style")
    private Styles top3Style;

    @Column(name = "top3_score")
    private Double top3Score;
}
