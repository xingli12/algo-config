package com.company.algo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger/OpenAPI 配置
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    /**
     * 创建 Swagger Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.company.algo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("算法配置管理系统 API")
                .description("提供算法配置、镜像管理、资源配置等核心功能的 REST API")
                .version("1.0.0")
                .contact(new Contact("Algo Config Team", "", ""))
                .build();
    }
}
