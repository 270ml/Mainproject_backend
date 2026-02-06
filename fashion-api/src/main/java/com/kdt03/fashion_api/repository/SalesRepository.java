package com.kdt03.fashion_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdt03.fashion_api.domain.Sales;

public interface SalesRepository extends JpaRepository<Sales, Integer> {
    List<Sales> findTop10ByOrderBySaleQuantityDesc();
}
