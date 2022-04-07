package com.example.bucao_springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Manager_info;
import com.example.bucao_springboot.mapper.Manager_infoMapper;
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
@RequestMapping("/Manager_info")

public class Manager_infoController {
    //将xxxx_infoMapper引入到xxxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Manager_infoMapper manager_infoMapper;


    //登录接口
    @PostMapping("/login")
    public Result<?> login(@RequestBody Manager_info manager_info)
    {

        Manager_info res=manager_infoMapper.selectOne(Wrappers.<Manager_info>lambdaQuery().eq(Manager_info::getId,manager_info.getId()).eq(Manager_info::getPsd,manager_info.getPsd()));

        if(res == null)
        {
            return Result.error("-1","用户名或密码错误");
        }

        return Result.success(res);
    }
    //新增接口
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成manager_info类
    public Result<?> save(@RequestBody Manager_info manager_info)
    {
        try {
            Manager_info user=manager_infoMapper.selectOne(Wrappers.<Manager_info>lambdaQuery().eq(Manager_info::getId,manager_info.getId()).or().eq(Manager_info::getTelephone,manager_info.getTelephone()));
            if(user==null) {
                manager_infoMapper.insert(manager_info);
                System.out.println("Manager_info已添加用户"+manager_info.getId()+"信息：");
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
    public Result<?> update(@RequestBody Manager_info manager_info)
    {
        manager_infoMapper.updateById(manager_info);
        System.out.println("Manager_info已更新用户"+manager_info.getId()+"的信息：");
        return Result.success();
    }

    //删除接口
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id)
    {
        manager_infoMapper.deleteById(id);
        System.out.println("Manager_info已删除用户"+id+"的信息：");
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

        LambdaQueryWrapper<Manager_info> wrapper = Wrappers.<Manager_info>lambdaQuery();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(Manager_info::getId,search);//eq(a,b)<=>a=b
        }
        Page<Manager_info> Manager_info_page=manager_infoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(Manager_info_page);
    }

    //显示个人信息
    @GetMapping("/{id}")
    public Result<?> SelectPerson_Info(@PathVariable String id)
    {
        manager_infoMapper.selectById(id);
        System.out.println("Manager_info已查询到用户"+id+"的信息：");
        return Result.success();
    }


    /**批量删除接口:复合主键
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<String> ids) {

        manager_infoMapper.deleteBatchIds(ids);
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

        List<Manager_info> all = manager_infoMapper.selectList(null);
        for (Manager_info Manager_info : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("账号", Manager_info.getId());
            row1.put("姓名", Manager_info.getMname());
            row1.put("性别", Manager_info.getSex());
            row1.put("权限",Manager_info.getPri());
            row1.put("头像", Manager_info.getPortrait());
            row1.put("联系电话", Manager_info.getTelephone());
            row1.put("地址", Manager_info.getAddress());
            row1.put("邮件", Manager_info.getEmail());
            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息数据表", "UTF-8");
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
        System.out.println(lists);
        List<Manager_info> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Manager_info Manager_info = new Manager_info();
            Manager_info.setId(row.get(0).toString());
            Manager_info.setMname(row.get(1).toString());
            Manager_info.setSex(row.get(2).toString());
            Manager_info.setPri(row.get(3).toString());
            Manager_info.setPortrait(row.get(4).toString());
            Manager_info.setTelephone(row.get(4).toString());
            Manager_info.setAddress(row.get(5).toString());
            Manager_info.setPsd(row.get(0).toString());
            Manager_info.setEmail(row.get(6).toString());

            saveList.add(Manager_info);
        }
        for (Manager_info Manager_info : saveList) {

            if(Manager_info.getId()!=null)
            {
               manager_infoMapper.insert(Manager_info);
            }
        }
        return Result.success();
    }
}
