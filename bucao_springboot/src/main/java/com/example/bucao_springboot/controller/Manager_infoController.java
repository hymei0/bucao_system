package com.example.bucao_springboot.controller;

import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Manager_info;
import com.example.bucao_springboot.mapper.Manager_infoMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Manager_info")

public class Manager_infoController {
    //将xxxx_infoMapper引入到xxxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Manager_infoMapper manager_infoMapper;
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成manager_info类
    public Result<?> save(@RequestBody Manager_info manager_info)
    {
        manager_infoMapper.insert(manager_info);// 将数据插入到数据库里面，实现新增
        return Result.success();
    }
}
