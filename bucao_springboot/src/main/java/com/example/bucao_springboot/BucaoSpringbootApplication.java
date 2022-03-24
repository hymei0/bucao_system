package com.example.bucao_springboot;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.example.bucao_springboot.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = "com.example.bucao_springboot.mapper")

public class BucaoSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BucaoSpringbootApplication.class, args);
    }

}
