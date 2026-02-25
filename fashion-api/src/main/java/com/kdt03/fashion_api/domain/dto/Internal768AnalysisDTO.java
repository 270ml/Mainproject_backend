package com.kdt03.fashion_api.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Internal768AnalysisDTO {
    private List<Double> embedding;
    private Integer dimension;
    private List<StyleScoreDTO> styles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StyleScoreDTO {
        private String style;
        private Double score;
    }
}
