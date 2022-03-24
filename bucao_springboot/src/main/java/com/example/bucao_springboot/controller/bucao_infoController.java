package com.example.bucao_springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.entity.User_info;
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
        System.out.println(bucao_info.getRfid());
        try{
            QueryWrapper<Bucao_info> wrapper=new QueryWrapper<>();
            wrapper.eq("rfno", bucao_info.getRfno()).eq("rfid", bucao_info.getRfid());
            Bucao_info result=bucao_infoMapper.selectOne(wrapper);
            if(result==null) {
                bucao_infoMapper.insert(bucao_info);
                return Result.success();
            }
            else{
                return Result.error("-1","该布草已经存在");
            }
        }catch (Exception e){
            return Result.error("-1",e.toString());
        }
    }

    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody Bucao_info bucao_info)
    {
        // Bucao_info bucao=bucao_infoMapper.lambdaUpdate().eq(Entity::getId, value).update(entity);
        bucao_infoMapper.update(bucao_info,Wrappers.<Bucao_info>lambdaUpdate().eq(Bucao_info::getRfno,bucao_info.getRfno()).eq(Bucao_info::getRfid,bucao_info.getRfid()));
        System.out.println(bucao_info.getRfno());
        return Result.success();
    }

    //删除接口
    @DeleteMapping
    public Result<?> delete(@RequestParam String rfno,
                            @RequestParam String rfid)
    {
        System.out.println(rfno);
        try {
            // Bucao_info bucao=bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,bucao_info.getRfno()).eq(Bucao_info::getRfid,bucao_info.getRfid()));
            QueryWrapper<Bucao_info> wrapper = new QueryWrapper<>();

            wrapper.eq("rfno", rfno).eq("rfid", rfid);
            int rows = bucao_infoMapper.delete(wrapper);
            return Result.success();
        }catch (Exception e){
            return Result.error("-1",e.toString());
        }
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

        //LambdaQueryWrapper<Bucao_info> wrapper = Wrappers.<Bucao_info>lambdaQuery();
        QueryWrapper<Bucao_info> wrapper=new QueryWrapper<>();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {

            wrapper.like("rfid",search).or().like("rfno",search).or().like("state",search);//eq(a,b)<=>a=b
        }
        Page<Bucao_info> bucao_info_page=bucao_infoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(bucao_info_page);
    }

}
