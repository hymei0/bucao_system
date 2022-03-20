package com.example.bucao_springboot.controller;


import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.mapper.Bucao_infoMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Bucao_info")

public class bucao_infoController {
    //将Bucao_infoMapper引入到bucao_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_infoMapper bucao_infoMapper;
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成Bucao_info类
    public Result<?> save(@RequestBody Bucao_info bucao_info)
    {
        bucao_infoMapper.insert(bucao_info);// 将数据插入到数据库里面，实现新增
        return Result.success();
    }
}
