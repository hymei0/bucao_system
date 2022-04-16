package com.example.bucao_springboot.common;

import com.example.bucao_springboot.common.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
// alipay没有携带token,但是我们限制SDK接口调用，为其放行；
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/rfid_kinds/**","/rfid_kinds",
                        "/Bucao_info/**","/Bucao_info","/Bucao_room/**","/Bucao_room",
                        "/Bucao_user","/Bucao_user/**","/Order","/Order/**",
                        "/User_info/**", "/User_info","/Room_info","/Room_info/**",
                        "/Section","/Section/**","/User_room","/User_room/**",
                        "/imserver/**", "/files/**","/Bucao_info",
                        "/alipay/**", "/doc.html", "/webjars/**", "/swagger-resources/**"
                );

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置拦截器访问静态资源
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
