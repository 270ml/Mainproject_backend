package com.kdt03.fashion_api.client;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.kdt03.fashion_api.domain.dto.Internal768AnalysisDTO;

@HttpExchange
public interface InternalFastApiClient {

    @PostExchange("/analyze")
    Internal768AnalysisDTO analyzeInternal768(@RequestPart("file") Resource file);
}
