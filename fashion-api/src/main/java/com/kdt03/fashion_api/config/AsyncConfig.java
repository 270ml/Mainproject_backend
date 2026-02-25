package com.kdt03.fashion_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // spring.threads.virtual.enabled=true 설정으로 인해
    // 별도의 가상 스레드 Executor 빈 등록 없이도 @Async 등에 자동 적용됩니다.
    // 특수한 목적의 커스텀 Executor가 필요한 경우에만 추가합니다.
}
