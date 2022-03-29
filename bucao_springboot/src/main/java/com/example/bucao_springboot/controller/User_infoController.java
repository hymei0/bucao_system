package com.example.bucao_springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.User_info;
import com.example.bucao_springboot.mapper.User_infoMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/User_info")
public class User_infoController {
    @Resource
    User_infoMapper user_infoMapper;

    //登录接口
    @PostMapping("/login")
    public Result<?> login(@RequestBody User_info user_info)
    {

        User_info res=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()).eq(User_info::getPsd,user_info.getPsd()));

        if(res == null)
        {
            return Result.error("-1","用户名或密码错误");
        }

        return Result.success(res);
    }
    //注册接口
    @PostMapping("/register")
    public Result<?> register(@RequestBody User_info user_info)
    {
        System.out.println(user_info.getID());
        try{
            User_info user=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()).or().eq(User_info::getTelephone,user_info.getTelephone()));
            if(user==null) {
                user_infoMapper.insert(user_info);
                return Result.success();
            }
            else{
                return Result.error("-1","该账号/或该电话已存在");
            }
        }catch (Exception e){
            return Result.error("-1","系统错误，请稍后重试");
        }
    }
    //新增接口
    @PostMapping
    public Result<?> save(@RequestBody User_info user_info)
    {
        try {
            User_info user=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()).or().eq(User_info::getTelephone,user_info.getTelephone()));
            if(user==null) {
                user_infoMapper.insert(user_info);
                System.out.println(user_info.getID());
                return Result.success();
            }
            else
            {
                return Result.error("-1","该用户已存在");
            }
        }catch (Exception e)
        {
            return Result.error("-1","系统后台出错啦，请联系工作人员");
        }
    }
    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody User_info user_info)
    {
        try {
            User_info user=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()).or().eq(User_info::getTelephone,user_info.getTelephone()));
            if(user==null) {
                user_infoMapper.updateById(user_info);
                System.out.println(user_info.getID());
                return Result.success();
            }else{
                return Result.error("-1","该账号或电话号码已存在");
            }
        }catch (Exception e){
            return Result.error("-1","系统后台出错，请联系管理员");
        }
    }

    //删除接口
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id)
    {
        user_infoMapper.deleteById(id);
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
        // Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        // LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        LambdaQueryWrapper<User_info> wrapper = Wrappers.<User_info>lambdaQuery();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(User_info::getID,search);//eq(a,b)<=>a=b
        }
        Page<User_info> user_info_page=user_infoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(user_info_page);
    }

    //显示个人信息
    @GetMapping("/{id}")
    public Result<?> SelectPerson_Info(@PathVariable String id)
    {
        user_infoMapper.selectById(id);
        return Result.success();
    }
}

