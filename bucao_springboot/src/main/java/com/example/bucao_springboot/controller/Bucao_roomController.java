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
import com.example.bucao_springboot.entity.Bucao_room;
import com.example.bucao_springboot.mapper.Bucao_roomMapper;
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
@RequestMapping("/Bucao_room")

public class Bucao_roomController {
    //将xxx_infoMapper引入到xxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_roomMapper bucao_roomMapper;

    //新增接口
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成xxx类
    public Result<?> save(@RequestBody Bucao_room Bucao_room)
    {
        try{
            QueryWrapper<Bucao_room> wrapper=new QueryWrapper<>();
            wrapper.eq("rfno", Bucao_room.getRfno()).eq("rfid", Bucao_room.getRfid()).eq("room_id",Bucao_room.getRoomId());
            Bucao_room result=bucao_roomMapper.selectOne(wrapper);
            if(result==null) {
                bucao_roomMapper.insert(Bucao_room);
                return Result.success();
            }
            else{
                return Result.error("-1","该记录已存在，如有变更需求请进行修改操作！");
            }
        }catch (Exception e){
            return Result.error("-1","后台错啦，请联系开发人员");
        }
    }

    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody Bucao_room bucao_room)
    {
        try {
            bucao_roomMapper.update(bucao_room, Wrappers.<Bucao_room>lambdaUpdate().eq(Bucao_room::getRfno, bucao_room.getRfno()).eq(Bucao_room::getRfid, bucao_room.getRfid()).eq(Bucao_room::getRoomId, bucao_room.getRoomId()));
            return Result.success();
        }catch (Exception e){
            return Result.error("-1","更新错误");
        }

    }

    //删除接口
    @DeleteMapping
    public Result<?> delete(@RequestParam String roomId,
                            @RequestParam String rfno,
                            @RequestParam String rfid)
    {

        try {
            // Bucao_room bucao=Bucao_roomMapper.selectOne(Wrappers.<Bucao_room>lambdaQuery().eq(Bucao_room::getRfno,Bucao_room.getRfno()).eq(Bucao_room::getRfid,Bucao_room.getRfid()));
            QueryWrapper<Bucao_room> wrapper = new QueryWrapper<>();

            wrapper.eq("rfno", rfno).eq("rfid", rfid).eq("room_id",roomId);
            int rows = bucao_roomMapper.delete(wrapper);
            System.out.println(roomId+","+rfid+rfno+"该记录删除成功");
            return Result.success();
        }catch (Exception e){
            return Result.error("-1","后台出错啦，请联系开发人员");
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

        //LambdaQueryWrapper<Bucao_room> wrapper = Wrappers.<Bucao_room>lambdaQuery();

        QueryWrapper<Bucao_room> wrapper=new QueryWrapper<>();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {

            wrapper.like("rfid",search).or().like("rfno",search).or().like("roomId",search);//eq(a,b)<=>a=b
        }
        Page<Bucao_room> Bucao_room_page=bucao_roomMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(Bucao_room_page);
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
            QueryWrapper<Bucao_room> wrapper = new QueryWrapper<>();
            wrapper.eq("rfno", id.get(0)).eq("rfid", id.get(1)).eq("room_id",id.get(2));
            bucao_roomMapper.delete(wrapper);
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

        List<Bucao_room> all = bucao_roomMapper.selectList(null);
        for (Bucao_room Bucao_room : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("病房号", Bucao_room.getRoomId());
            row1.put("布草RFID编号", Bucao_room.getRFIDX());
            row1.put("数量", Bucao_room.getNum());

            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("布草分布信息数据表", "UTF-8");
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
        List<Bucao_room> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Bucao_room Bucao_room = new Bucao_room();
            Bucao_room.setRfno(row.get(0).toString());
            Bucao_room.setRfid(row.get(1).toString());

            saveList.add(Bucao_room);
        }
        for (Bucao_room Bucao_room : saveList) {
            System.out.println(Bucao_room);
            if(Bucao_room.getRfid()!=null&&Bucao_room.getRfno()!=null)
            {
                bucao_roomMapper.insert(Bucao_room);
            }
        }
        return Result.success();
    }

}
