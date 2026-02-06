package com.kdt03.fashion_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kdt03.fashion_api.domain.dto.NaverProductDTO;
import com.kdt03.fashion_api.repository.NaverProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverProductService {

    private final NaverProductRepository naverProductRepo;

    public List<NaverProductDTO> getAllNaverProducts() {
        return naverProductRepo.findAll().stream()
                .map(NaverProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
