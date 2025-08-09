package com.library.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NaverClientConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor(@Value("${external.naver.headers.client-id}") String naverClientId,
                                                 @Value("${external.naver.headers.client-secret}") String naverClientSecret) {

        return requestTemplate -> requestTemplate.header("X-Naver-Client-Id", naverClientId)
            .header("X-Naver-Client-Secret", naverClientSecret);
        };
    }

