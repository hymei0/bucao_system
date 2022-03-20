package com.example.bucao_springboot.common;

//import  com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;PaginationInnerInterceptor
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.springboot_.mapper")
public class MybatisPlusConfig {
    /*
    *分页插件
    * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false
    * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
    *@Bean 旧版
    public PaginationInnerInterceptor paginationInnerInterceptor()
    {
        return new PaginationInnerInterceptor();
    }
     */
    @Bean //最新版
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


}
