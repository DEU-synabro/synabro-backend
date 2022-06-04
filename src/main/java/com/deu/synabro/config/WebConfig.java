package com.deu.synabro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/*
 * 이 클래스는 Swagger UI 및 Cors 정책과 관련된
 * 초기 설정을 하기 위한 클래스이다.
 * @author Jiyoon Bak
 * @version 1.0
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /*
     * Swagger 설정의 핵심이 되는 Bean으로
     * api의 그룹명이나 이동경로, 보여질 api가 속한 패키지 등의
     * 자세한 정보를 담습니다.
     *
     * @return API 문서 적용될 설정
     * @see springfox.documentation.spring.web.plugins.Docket
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .maxAge(3000);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .securityContexts(Stream.of(securityContext()).collect(Collectors.toList()))
                .securitySchemes(Collections.singletonList(securitySchema()))
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.deu.synabro"))
                    .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    /*
     * Swagger를 이용한 API 문서의
     * 대표적인 내용(제목, 설명, 버전 등)을 표시할 수 있도록
     * ApiInfo 객체를 생성한다.
     *
     * @return API 문서에 적용될 내용들
     * @see springfox.documentation.service.ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Synabro")
                .description("Synabro API docs")
                .version("1.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(
                        oc -> oc.requestMappingPattern().matches("((?!boards|sign).)*")
                )
                .build();
    }

    private HttpAuthenticationScheme securitySchema() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("JWT Token").build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> jwt = new ArrayList<>();
        jwt.add(new SecurityReference("JWT Token", authorizationScopes));
        return jwt;
    }
}