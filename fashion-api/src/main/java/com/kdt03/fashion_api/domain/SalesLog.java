package com.kdt03.fashion_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sales_log")
@Getter
@NoArgsConstructor
public class SalesLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "sale_quantity", nullable = false)
    private Long saleQuantity;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;
}
