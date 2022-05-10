package com.example.bucao_springboot.common;




import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30) // v2 不同
                .apiInfo(ApiInfo())
                .select()
                //设置通过什么方式定位需要自动生成文档的接口，这里定位方法上的@ApiOperation
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.example.bucao_springboot.controller")) // 设置扫描路径
                .build();
    }

    private ApiInfo ApiInfo() {
        return new ApiInfoBuilder()
                .title("布草智能柜后台管理系统--api文档")
                .description("布草智能柜后台管理系统接口描述")
                .version("1.0")
                .contact(new Contact("何元梅","http://baidu.com","3304534355@qq.com"))
                .build();
    }

}
