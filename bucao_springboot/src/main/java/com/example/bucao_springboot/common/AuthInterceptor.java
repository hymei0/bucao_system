package com.example.bucao_springboot.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import com.example.bucao_springboot.entity.User_info;

import com.example.bucao_springboot.exception.CustomException;
import com.example.bucao_springboot.mapper.User_infoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 拦截器：未登录时不登录访问后台接口

public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private User_infoMapper user_infoMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            throw new CustomException("401", "未获取到token, 请重新登录");
        }
        String userId = JWT.decode(token).getAudience().get(0);
        User_info User_info = user_infoMapper.selectById(userId);
        if (User_info == null) {
            throw new CustomException("401", "token不合法");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(User_info.getPsd())).build();
        try {
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new CustomException("401", "token不合法");
        }
        return true;
    }
}
