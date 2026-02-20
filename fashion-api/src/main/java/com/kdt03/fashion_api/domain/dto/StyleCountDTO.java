package com.kdt03.fashion_api.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StyleCountDTO {
    private String styleName;
    private Long count;

    public StyleCountDTO(String styleName, Long count) {
        this.styleName = styleName;
        this.count = count;
    }
}
