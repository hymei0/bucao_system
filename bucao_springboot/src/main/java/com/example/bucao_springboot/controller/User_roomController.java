package com.example.bucao_springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.User_room;
import com.example.bucao_springboot.mapper.User_roomMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/User_room")
public class User_roomController {
    @Resource
    User_roomMapper User_roomMapper;

    //新增接口
    @PostMapping
    public Result<?> save(@RequestBody User_room user_room)
    {
        try {
            User_room user=User_roomMapper.selectOne(Wrappers.<User_room>lambdaQuery().eq(User_room::getUserid,user_room.getUserid()).eq(User_room::getRoomid,user_room.getRoomid()));
            if(user==null) {
                User_roomMapper.insert(user_room);
                System.out.println("User_room已添加用户"+user_room.getUserid()+"的住院信息：");
                return Result.success();
            }
            else
            {
                return Result.error("-1","该用户已存在");
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","系统后台出错啦，请联系工作人员");
        }
    }
    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody User_room User_room)
    {
        try {
            System.out.println(User_room);
            User_roomMapper.update(User_room.getComeTime(),User_room.getOutTime(),User_room.getExpenses(),User_room.getRoomid(),User_room.getUserid());
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","更新失败");
        }

    }


    //删除接口
    @DeleteMapping
    public Result<?> delete(@RequestParam String userid,
                            @RequestParam String roomid)
    {
        try {
            // Bucao_info bucao=bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,bucao_info.getRfno()).eq(Bucao_info::getRfid,bucao_info.getRfid()));
            QueryWrapper<User_room> wrapper = new QueryWrapper<>();

            wrapper.eq("userid", userid).eq("roomid", roomid);
            int rows=User_roomMapper.delete(wrapper);
            //int rows = User_roomMapper.delete1(userid,roomid);
            return Result.success();
        }catch (Exception e){
            return Result.error("-1",e.toString());
        }
    }

    //无条件查询
    @GetMapping("/selectall")
    public Result<?>  selectall(){
        return Result.success(User_roomMapper.GetUser_room());
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {

        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            Page<User_room> User_room_page=User_roomMapper.findPage(new Page<>(pageNum,pageSize),search);
            return Result.success(User_room_page);
        }
        else{
            Page<User_room> User_room_page=User_roomMapper.findPage1(new Page<>(pageNum,pageSize));
            return Result.success(User_room_page);
        }
    }


    /**批量删除接口:复合主键
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<List<String>> ids) {

        for(List<String> id:ids)
        {
            QueryWrapper<User_room> wrapper = new QueryWrapper<>();
            wrapper.eq("userid", id.get(0)).eq("roomid", id.get(1));
            User_roomMapper.delete(wrapper);
        }
        return Result.success();
    }

    /**
     * Excel导出
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<User_room> all = User_roomMapper.selectList(null);
        for (User_room User_room : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("证件号", User_room.getUserid());
            row1.put("姓名", User_room.getUname());
            row1.put("性别", User_room.getSex());
            row1.put("联系电话", User_room.getTelephone());
            row1.put("病房号", User_room.getRoomid());
            row1.put("入院时间", User_room.getComeTime());
            row1.put("出院时间", User_room.getOutTime());
            row1.put("应缴费用", User_room.getExpenses());
            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("住院信息数据表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

}
