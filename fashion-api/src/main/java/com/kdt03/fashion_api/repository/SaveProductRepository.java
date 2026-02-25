package com.kdt03.fashion_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdt03.fashion_api.domain.SaveProducts;

public interface SaveProductRepository extends JpaRepository<SaveProducts, Long> {
    List<SaveProducts> findByMemberId(String memberId);

    Optional<SaveProducts> findBySaveIdAndMemberId(Long saveId, String memberId);

    List<SaveProducts> findBySaveIdInAndMemberId(List<Long> saveIds, String memberId);

    List<SaveProducts> findByMemberIdAndNaverProductIdIn(String memberId, List<String> naverProductIds);

    boolean existsByMemberIdAndNaverProductId(String memberId, String naverProductId);
}
