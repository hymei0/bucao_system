package com.example.bucao_springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
@EntityScan(basePackages = "com.example.bucao_springboot.mapper")

@MapperScan(basePackages = "com.example.bucao_springboot.mapper")
public class BucaoSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BucaoSpringbootApplication.class, args);
    }

}
