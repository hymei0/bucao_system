package com.example.bucao_springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.mapper.Bucao_infoMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Bucao_info")

public class bucao_infoController {
    //将Bucao_infoMapper引入到bucao_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_infoMapper bucao_infoMapper;

    //新增接口
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成Bucao_info类
    public Result<?> save(@RequestBody Bucao_info bucao_info)
    {
        bucao_infoMapper.insert(bucao_info);// 将数据插入到数据库里面，实现新增
        return Result.success();
    }

    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody Bucao_info bucao_info)
    {
        bucao_infoMapper.updateById(bucao_info);
        System.out.println(bucao_info.getRfno());
        return Result.success();
    }

    //删除接口
    @DeleteMapping
    public Result<?> delete(@RequestBody Bucao_info bucao_info)
    {
        bucao_infoMapper.deleteById(bucao_info);
        System.out.println(bucao_info.getRfno());
        return Result.success();
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        //Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        //LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        LambdaQueryWrapper<Bucao_info> wrapper = Wrappers.<Bucao_info>lambdaQuery();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(Bucao_info::getRfno,search);//eq(a,b)<=>a=b
        }
        Page<Bucao_info> bucao_info_page=bucao_infoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(bucao_info_page);
    }

}
