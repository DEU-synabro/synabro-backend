package com.deu.synabro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


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
     * 개발 환경에서의 크로스 도메인 이슈를 해결하기 위한 코드로
     * 윤영 환경에 배포할 경우 addCorsMappings() 부분은 주석 처리합니다.
     *
     * ※ 크로스 도메인 이슈 : 브라우저에서 다른 도메인으로 URL 요청을 하는 경우 나타나는 보안 문제
     */
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000");
    }
    */

    /*
     * Swagger 설정의 핵심이 되는 Bean으로
     * api의 그룹명이나 이동경로, 보여질 api가 속한 패키지 등의
     * 자세한 정보를 담습니다.
     *
     * @return API 문서 적용될 설정
     * @see springfox.documentation.spring.web.plugins.Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.deu.synabro"))
                    .paths(PathSelectors.any())
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
}