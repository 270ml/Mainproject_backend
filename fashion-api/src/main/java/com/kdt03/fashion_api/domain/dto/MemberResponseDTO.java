package com.kdt03.fashion_api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {
    private String id;
    private String nickname;
    private String profile;
    private String provider;
}
