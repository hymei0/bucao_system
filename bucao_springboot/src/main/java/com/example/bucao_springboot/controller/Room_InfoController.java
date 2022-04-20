package com.example.bucao_springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.entity.Room_info;
import com.example.bucao_springboot.entity.Room_info;
import com.example.bucao_springboot.mapper.Room_infoMapper;
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
@RequestMapping("/Room_info")

public class Room_InfoController {
    //将xxxx_infoMapper引入到xxxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Room_infoMapper room_infoMapper;

    /**新增接口
     *
     * @param room_info
     * @return
     */
    @PostMapping
    public Result<?> save(@RequestBody Room_info room_info)
    {
        try {
            Room_info user=room_infoMapper.selectOne(Wrappers.<Room_info>lambdaQuery().eq(Room_info::getId,room_info.getId()));
            if(user==null) {
                room_infoMapper.insert(room_info);
                System.out.println("Room_info已添加病房"+room_info.getId()+"信息：");
                return Result.success();
            }
            else
            {
                return Result.error("-1","该病房已存在");
            }
        }catch (Exception e)
        {
            return Result.error("-1","系统后台出错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param Room_info
     * @return
     */
    @PutMapping
    public Result<?> update(@RequestBody Room_info Room_info)
    {
        try {
            room_infoMapper.updateById(Room_info);
            System.out.println("Room_info已更新病房" + Room_info.getId() + "的信息：");
            return Result.success();
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","请先删除请先删除住院相应住院记录和布草的分布记录");
        }
    }

    /**删除接口
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id)
    {
        try {
            room_infoMapper.deleteById(id);
            System.out.println("Room_info已删除病房" + id + "的信息：");
            return Result.success();
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","请先删除请先删除相关住院记录和布草分布记录");
        }
    }

    /**分页查询
     *
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @param search 查询关键字
     * @return
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        // Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        // LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        LambdaQueryWrapper<Room_info> wrapper = Wrappers.<Room_info>lambdaQuery();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(Room_info::getId,search).or().like(Room_info::getSection,search);//eq(a,b)<=>a=b
        }
        Page<Room_info> Room_info_page=room_infoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(Room_info_page);
    }

    /**批量查询所属病房接口
     *
     * @param ids
     * @return
     */
    @GetMapping("/allsection")
    public Result<?> allSections(@RequestBody List<String> ids) {

        room_infoMapper.selectBatchIds(ids);
        return Result.success();
    }

    /**显示病房信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<?> SelectRoom_Info(@PathVariable String id)
    {

         Room_info room= room_infoMapper.selectById(id);
        System.out.println("Room_info已查询到病房"+id+"的信息：");
        return Result.success(room);
    }

    /**无条件查询
     *
     * @return
     */
    @GetMapping("/selectall")
    public Result<?>  selectall(){
        QueryWrapper<Room_info> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=room_infoMapper.selectMaps(queryWrapper);
        return Result.success(list);
    }

    /**查询出没有分配足够到的布草的病房
     *
     * @return
     */
    @GetMapping("/forbucao")
    public Result<?>  selecforbucao(){
        QueryWrapper<Room_info> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=room_infoMapper.selectforbucao();
        return Result.success(list);
    }


    /**批量删除接口:复合主键
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<String> ids) {

        room_infoMapper.deleteBatchIds(ids);
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

        List<Room_info> all = room_infoMapper.selectList(null);
        for (Room_info Room_info : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("病房号", Room_info.getId());
            row1.put("所属部门", Room_info.getSection());
            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("病房信息数据表", "UTF-8");
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
        Integer num = 0;//统计导入成功的记录条数
        try {
            InputStream inputStream = file.getInputStream();
            List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
            System.out.println(lists);
            List<Room_info> saveList = new ArrayList<>();
            for (List<Object> row : lists) {
                Room_info Room_info = new Room_info();
                Room_info.setId(row.get(0).toString());
                Room_info.setSection(row.get(1).toString());
                saveList.add(Room_info);
            }
            for (Room_info Room_info : saveList) {
                if (Room_info.getId() != null) {
                    room_infoMapper.insert(Room_info);
                    num = num + 1;
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return Result.success(num);
    }

}
