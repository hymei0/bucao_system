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
import com.example.bucao_springboot.entity.Bucao_user;
import com.example.bucao_springboot.entity.User_room;
import com.example.bucao_springboot.mapper.Bucao_userMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Bucao_user")

public class Bucao_userController {
    //将xxx_infoMapper引入到xxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_userMapper bucao_userMapper;
    //新增接口
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成xxx类
    public Result<?> save(@RequestBody Bucao_user bucao_user)
    {
        try{
            QueryWrapper<Bucao_user> wrapper=new QueryWrapper<>();
            wrapper.eq("rfno", bucao_user.getRfno()).eq("rfid", bucao_user.getRfid()).eq("user_id",bucao_user.getUserId());
            Bucao_user result=bucao_userMapper.selectOne(wrapper);
            if(result==null) {
                bucao_userMapper.updatebucao(bucao_user.getRfno(),bucao_user.getRfid());
                bucao_userMapper.insert(bucao_user);
                return Result.success();
            }
            else{
                return Result.error("-1","该记录已存在，如有变更需求请进行修改操作！");
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台错啦，请联系开发人员");
        }
    }

    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody Bucao_user bucao_user)
    {
        try {
            bucao_userMapper.update(bucao_user, Wrappers.<Bucao_user>lambdaUpdate().eq(Bucao_user::getRfno, bucao_user.getRfno()).eq(Bucao_user::getRfid, bucao_user.getRfid()).eq(Bucao_user::getUserId, bucao_user.getUserId()));
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }

    }

    //删除接口
    @DeleteMapping
    public Result<?> delete(@RequestParam String UserId,
                            @RequestParam String rfno,
                            @RequestParam String rfid)
    {

        try {
            // Bucao_user bucao=Bucao_userMapper.selectOne(Wrappers.<Bucao_user>lambdaQuery().eq(Bucao_user::getRfno,Bucao_user.getRfno()).eq(Bucao_user::getRfid,Bucao_user.getRfid()));
            QueryWrapper<Bucao_user> wrapper = new QueryWrapper<>();

            wrapper.eq("rfno", rfno).eq("rfid", rfid).eq("room_id",UserId);
            int rows = bucao_userMapper.delete(wrapper);
            System.out.println(UserId+","+rfid+rfno+"该记录删除成功");
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错啦，请联系开发人员");
        }
    }
    //分页查询:面向用户的接口
    @GetMapping("foruser")
    public Result<?> findPageuser(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestParam(defaultValue = "") String userid)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        try {
            QueryWrapper<Bucao_user> wrapper = new QueryWrapper<>();

            if (StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
            {
                wrapper.eq("user_id", userid).like("rfid", search).or().like("rfno", search).or().like("user_id", search).or().like("room_id", search).or().like("user_name", search);//eq(a,b)<=>a=b
            } else {
                wrapper.eq("user_id", userid);
            }
            Page<Bucao_user> Bucao_user_page = bucao_userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
            return Result.success(Bucao_user_page);
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }
    }

    //分页查询:面向管理员的接口
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        //Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        //LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        //LambdaQueryWrapper<Bucao_user> wrapper = Wrappers.<Bucao_user>lambdaQuery();

        QueryWrapper<Bucao_user> wrapper=new QueryWrapper<>();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {

            wrapper.like("rfid",search).or().like("rfno",search).or().like("user_id",search).or().like("room_id",search).or().like("user_name",search);//eq(a,b)<=>a=b
        }
        Page<Bucao_user> Bucao_user_page=bucao_userMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(Bucao_user_page);
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
            QueryWrapper<Bucao_user> wrapper = new QueryWrapper<>();
            wrapper.eq("rfno", id.get(0)).eq("rfid", id.get(1)).eq("room_id",id.get(2));
            bucao_userMapper.delete(wrapper);
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

        List<Bucao_user> all = bucao_userMapper.selectList(null);
        for (Bucao_user Bucao_user : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("用户账号", Bucao_user.getUserId());
            row1.put("用户姓名", Bucao_user.getUserName());
            row1.put("所在病房", Bucao_user.getRoomId());
            row1.put("布草RFID编号", Bucao_user.getRFIDX());

            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("布草-用户信息数据表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    /**
     * Excel导入
     * 导入的模板可以使用 Excel导出的文件
     *
     * @param file Excel
     * @return
     * @throws IOException
     */
    @PostMapping("/import")
    public Result<?> upload(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
        List<Bucao_user> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Bucao_user Bucao_user = new Bucao_user();
            Bucao_user.setRfno(row.get(0).toString());
            Bucao_user.setRfid(row.get(1).toString());

            saveList.add(Bucao_user);
        }
        for (Bucao_user Bucao_user : saveList) {
            System.out.println(Bucao_user);
            if(Bucao_user.getRfid()!=null&&Bucao_user.getRfno()!=null)
            {
                bucao_userMapper.insert(Bucao_user);
            }
        }
        return Result.success();
    }
}
