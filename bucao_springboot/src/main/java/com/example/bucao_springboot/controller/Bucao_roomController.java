package com.example.bucao_springboot.controller;

import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Bucao_room;
import com.example.bucao_springboot.mapper.Bucao_roomMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Bucao_room")

public class Bucao_roomController {
    //将xxx_infoMapper引入到xxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_roomMapper bucao_roomMapper;
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成xxx_info类
    public Result<?> save(@RequestBody Bucao_room bucao_room)
    {
        bucao_roomMapper.insert(bucao_room);// 将数据插入到数据库里面，实现新增
        return Result.success();
    }
}
