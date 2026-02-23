package com.kdt03.fashion_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kdt03.fashion_api.domain.SaveProducts;
import com.kdt03.fashion_api.domain.dto.SaveProductRequestDTO;
import com.kdt03.fashion_api.domain.dto.SaveProductResponseDTO;
import com.kdt03.fashion_api.repository.NaverProductRepository;
import com.kdt03.fashion_api.repository.SaveProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaveProductService {

    private final SaveProductRepository saveProductRepository;
    private final NaverProductRepository naverProductRepository;

    // 관심 상품 등록
    public void addSaveProduct(String memberId, SaveProductRequestDTO dto) {
        if (saveProductRepository.existsByMemberIdAndNaverProductId(memberId, dto.getNaverProductId())) {
            throw new IllegalStateException("이미 관심 상품에 등록된 상품입니다.");
        }
        SaveProducts saveProduct = SaveProducts.builder()
                .memberId(memberId)
                .naverProductId(dto.getNaverProductId())
                .build();
        saveProductRepository.save(saveProduct);
    }

    // 내 관심 상품 목록 조회
    public List<SaveProductResponseDTO> getMySaveProducts(String memberId) {
        return saveProductRepository.findByMemberId(memberId).stream()
                .map(save -> {
                    var naver = naverProductRepository.findById(save.getNaverProductId()).orElse(null);
                    if (naver == null) return null;
                    return new SaveProductResponseDTO(
                            save.getSaveId(),
                            save.getNaverProductId(),
                            naver.getTitle(),
                            naver.getPrice(),
                            naver.getImageUrl(),
                            naver.getProductLink(),
                            save.getCreatedAt()
                    );
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    // 관심 상품 삭제
    public void deleteSaveProduct(Long saveId, String memberId) {
        SaveProducts saveProduct = saveProductRepository.findBySaveIdAndMemberId(saveId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 관심 상품을 찾을 수 없습니다."));
        saveProductRepository.delete(saveProduct);
    }
}