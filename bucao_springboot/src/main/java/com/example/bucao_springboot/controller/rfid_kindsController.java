package com.example.bucao_springboot.controller;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.mapper.RFid_kindsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/rfid_kinds")
public class rfid_kindsController {
    @Resource   //将rfid_kindsMapper引入到bucao_infoController中
    RFid_kindsMapper rfid_kindsMapper;

    //新增接口
    @PostMapping
    public Result<?> save(@RequestBody RFid_kinds rfid_kinds)
    {
        rfid_kindsMapper.insert(rfid_kinds);
        System.out.println(rfid_kinds.getRFNO());
        return Result.success();
    }
    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody RFid_kinds rfid_kinds)
    {
        rfid_kindsMapper.updateById(rfid_kinds);
        System.out.println(rfid_kinds.getRFNO());
        return Result.success();
    }

    //删除接口
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id)
    {
        rfid_kindsMapper.deleteById(id);
        System.out.println(id);
        return Result.success();
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
     {
        //Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

         //LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));

         LambdaQueryWrapper<RFid_kinds> wrapper = Wrappers.<RFid_kinds>lambdaQuery();
         if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
         {
             wrapper.like(RFid_kinds::getRFNO,search);//eq(a,b)<=>a=b
         }
         Page<RFid_kinds> rfid_kinds_page=rfid_kindsMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(rfid_kinds_page);
    }

}
