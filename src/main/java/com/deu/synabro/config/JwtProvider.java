package com.deu.synabro.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("spring.jwt.secret")
    private String secretKey;

    private Long tokenValidMillisecond = 60 * 60 * 1000L;

}
