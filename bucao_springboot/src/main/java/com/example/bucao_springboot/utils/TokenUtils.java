package com.example.bucao_springboot.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bucao_springboot.entity.User_info;
import com.example.bucao_springboot.mapper.User_infoMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class TokenUtils {

    @Autowired
    private User_infoMapper User_infoMapper;

    private static User_infoMapper staticUser_infoMapper;

    @PostConstruct
    public void init() {
        staticUser_infoMapper = User_infoMapper;
    }

    /**
     * 生成token
     * @param User_info
     * @return
     */
    public static String genToken(User_info User_info) {
        System.out.println("我可以生成token");
        return JWT.create().withExpiresAt(DateUtil.offsetDay(new Date(), 1)).withAudience(User_info.getID().toString())
                .sign(Algorithm.HMAC256(User_info.getPsd()));
    }

    /**
     * 获取token中的用户信息
     * @return
     */
    public static User_info getUser_info() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            String aud = JWT.decode(token).getAudience().get(0);
            String User_infoId = aud;
            return staticUser_infoMapper.selectById(User_infoId);
        } catch (Exception e) {
            log.error("解析token失败", e);
            return null;
        }
    }
}
