package com.example.bucao_springboot.controller;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.mapper.Bucao_infoMapper;
import com.example.bucao_springboot.mapper.RFid_kindsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rfid_kinds")
public class rfid_kindsController {
    @Resource   //将rfid_kindsMapper引入到bucao_infoController中
    RFid_kindsMapper rfid_kindsMapper;

    //新增接口
    @PostMapping
    public Result<?> save(@RequestBody RFid_kinds rfid_kinds)
    {
        try{
            RFid_kinds result=rfid_kindsMapper.selectOne(Wrappers.<RFid_kinds>lambdaQuery().eq(RFid_kinds::getRFNO,rfid_kinds.getRFNO()));
            if(result==null) {
                rfid_kindsMapper.insert(rfid_kinds);
                return Result.success();
            }
            else{
                return Result.error("-1","该类布草已经存在");
            }
        }catch (Exception e){
            return Result.error("-1","系统错误，请稍后重试");
        }

    }
    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody RFid_kinds rfid_kinds)
    {
        try{
            RFid_kinds result=rfid_kindsMapper.selectOne(Wrappers.<RFid_kinds>lambdaQuery().eq(RFid_kinds::getRFNO,rfid_kinds.getRFNO()));
            if(result==null) {
                rfid_kindsMapper.updateById(rfid_kinds);
                return Result.success();
            }
            else{
                return Result.error("-1","该类布草已经存在");
            }
        }catch (Exception e){
            return Result.error("-1","系统错误，请稍后重试");
        }
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
    //与bucao_info表交互的接口
    @GetMapping("/bucaoinfo")
    public List<Map<String, Object>>  SeletTobucaoinfo()
    {
        QueryWrapper<RFid_kinds> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=rfid_kindsMapper.selectMaps(queryWrapper);

        return list;
    }

}
