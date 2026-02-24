package com.kdt03.fashion_api.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastApiAnalysisDTO {
    @JsonProperty("batch_size")
    private Integer batchSize;

    private String device;

    @JsonProperty("total_latency_ms")
    private Double totalLatencyMs;

    @JsonProperty("unknown_threshold")
    private Double unknownThreshold;

    @JsonProperty("gap_threshold")
    private Double gapThreshold;

    @JsonProperty("latent_dim")
    private Integer latentDim;

    @JsonProperty("latent_source")
    private String latentSource;

    @JsonProperty("latent_in_features")
    private Integer latentInFeatures;

    private List<Result> results;

    // 에러 처리를 위한 필드 유지
    private String error;
    private String status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String file;

        @JsonProperty("top1_score")
        private Double top1Score;

        @JsonProperty("top2_score")
        private Double top2Score;

        @JsonProperty("gap_top1_top2")
        private Double gapTop1Top2;

        private Boolean unknown;

        private List<LabelScore> topk;

        @JsonProperty("latent_vector")
        private List<Double> latentVector;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabelScore {
        @JsonProperty("label_id")
        private Integer labelId;

        @JsonProperty("label_name")
        private String labelName;

        private Double score;
    }
}
