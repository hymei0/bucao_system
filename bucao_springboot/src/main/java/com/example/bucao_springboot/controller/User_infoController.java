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
import com.example.bucao_springboot.common.excelconfig;
import com.example.bucao_springboot.entity.Section;
import com.example.bucao_springboot.entity.User_info;
import com.example.bucao_springboot.mapper.User_infoMapper;
import com.example.bucao_springboot.utils.TokenUtils;
import io.swagger.annotations.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/User_info")
@Api(tags = "用户信息管理接口")
public class User_infoController {
    @Resource
    User_infoMapper user_infoMapper;

    /**登录接口
     *
     * @param user_info
     * @return
     */

    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口",notes="传入的参数：实体类，其中密码和用户id不能为空")
    public Result<?> login(@RequestBody User_info user_info)
    {

        User_info res=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()).eq(User_info::getPsd,user_info.getPsd()));
        if(res == null)
        {
            return Result.error("-1","账号或密码错误");
        }
        System.out.println("User_info已登录到"+user_info.getID()+"的账户");
        // 生成token
        String token = TokenUtils.genToken(res);
        res.setToken(token);
        return Result.success(res);
    }

    /**注册接口
     *
     * @param user_info
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口")
    public Result<?> register(@RequestBody User_info user_info)
    {
        try{
            User_info user=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()).or().eq(User_info::getTelephone,user_info.getTelephone()));
            if(user==null) {
                user_infoMapper.insert(user_info);
                System.out.println("User_info已注册用户"+user_info.getID()+"的信息：");
                return Result.success();
            }
            else{
                return Result.error("-1","该账号已存在");
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }
    }

    /**无条件查询所有用户
     *
     * @return
     */
    @GetMapping("/selectall")
    @ApiOperation(value = "查询所有用户",notes="查询所有用户")
    public Result<?>  selectall(){
        QueryWrapper<User_info> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=user_infoMapper.selectMaps(queryWrapper);
        return Result.success(list);
    }

    /**新增接口
     *
     * @param user_info
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加用户接口",notes="添加用户")
    public Result<?> save(@RequestBody  @Valid User_info user_info)
    {
        try {
            User_info user=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()).or().eq(User_info::getTelephone,user_info.getTelephone()));
            if(user==null) {
                if(!user_info.getSex().equals("男") && !user_info.getSex().equals("女"))
                {
                    return Result.error("-1", "性别只能为男或者女");
                }
                user_info.setPsd(user_info.getID());
                user_infoMapper.insert(user_info);
                System.out.println("User_info已添加用户"+user_info.getID()+"信息：");
                return Result.success();
            }
            else
            {
                return Result.error("-1","该用户已存在");
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","系统后台出错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param user_info
     * @return
     */
    @PutMapping
    @ApiOperation(value = "用户信息更新接口",notes="更新用户信息")
    public Result<?> update(@RequestBody @Valid User_info user_info)
    {
        User_info user=user_infoMapper.selectOne(Wrappers.<User_info>lambdaQuery().eq(User_info::getID,user_info.getID()));
        if(user==null)
        {
            return  Result.error("-1","该用户不存在");
        }
        else {
            try {
                if(!user_info.getSex().equals("男") && !user_info.getSex().equals("女"))
                {
                    return Result.error("-1", "性别只能为男或者女");
                }
                user_infoMapper.updateById(user_info);
                System.out.println("User_info已更新用户" + user_info.getID() + "的信息：");
                return Result.success();
            } catch (Exception e) {
                System.out.println(e.toString());
                return Result.error("-1", "后台出错了，请联系开发人员");
            }
        }
    }

    /**删除接口
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户信息接口",notes="根据用户id删除用户信息")
    public Result<?> delete(@PathVariable String id)
    {
        User_info user=user_infoMapper.selectById(id);
        if(user==null)
        {
            return  Result.error("-1","该用户不存在");
        }else {
            try {
                user_infoMapper.deleteById(id);
                System.out.println("User_info已删除用户" + id + "的信息：");
                return Result.success();
            } catch (Exception e) {
                System.out.println(e.toString());
                return Result.error("-1", "请先删除布草-用户、住院信息表中的相关记录");
            }
        }
    }

    /**分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping
    @ApiOperation(value = "用户信息分页查找")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        // Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        // LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        if(pageNum<1){
            return Result.error("-1","pageNum不能小于1");
        }
        if(pageSize<1){
            return Result.error("-1","pageSize不能小于1");
        }
        LambdaQueryWrapper<User_info> wrapper = Wrappers.<User_info>lambdaQuery();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(User_info::getID,search).or().like(User_info::getTelephone,search).or().like(User_info::getAddress,search).or().like(User_info::getSex,search).or().like(User_info::getUname,search);//eq(a,b)<=>a=b
        }
        Page<User_info> user_info_page=user_infoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(user_info_page);
    }

    /**查询个人信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询个人信息",notes="通过用户的id查找用户信息")
    public Result<?> SelectPerson_Info(@PathVariable String id)
    {

        User_info user=user_infoMapper.selectById(id);
        if(user==null)
        {
            return Result.error("-1","该用户不存在");
        }
        else{
            System.out.println("User_info已查询到用户"+id+"的信息");
            return Result.success(user);
        }
    }


    /**批量删除接口
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除接口",notes="通过用户的id集合删除用户信息")
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<String> ids) {
        Integer suncess=0;
        try {
            for (int i = 0; i < ids.size(); i++) {
                if (user_infoMapper.selectById(ids.get(i)) != null&&user_infoMapper.selectUniqueuser(ids.get(i)).size()>0) {
                    user_infoMapper.deleteById(ids.get(i));
                    suncess++;
                }
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return Result.success(suncess);
    }

    /**
     * Excel导出
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    @ApiOperation(value = "Excel文件导出接口",notes="excel文件导出")
    public Result<?> export(HttpServletResponse response) throws IOException {

        try {
            List<Map<String, Object>> list = CollUtil.newArrayList();
            List<User_info> all = user_infoMapper.selectList(null);
            for (User_info user_info : all) {
                Map<String, Object> row1 = new LinkedHashMap<>();
                row1.put("证件号", user_info.getID());
                row1.put("姓名", user_info.getUname());
                row1.put("性别", user_info.getSex());
                row1.put("头像", user_info.getPortrait());
                row1.put("联系电话", user_info.getTelephone());
                row1.put("地址", user_info.getAddress());
                list.add(row1);
            }
            // 2. 写excel
            ExcelWriter writer = ExcelUtil.getWriter(true);
            writer.write(list, true);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String fileName = URLEncoder.encode("用户信息数据表", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            writer.close();
            IoUtil.close(System.out);
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","导出失败");
        }

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
    @ApiOperation(value = "excel文件导入接口",notes="excel文件导入")
    public Result<?> upload(@ApiParam(value = "选择excel文件") @Valid @RequestPart(value = "file") MultipartFile file) throws IOException {
        Integer success=0;;//统计导入成功的记录条数
        excelconfig ex = new excelconfig();
        if (ex.getFileType(file.getOriginalFilename())==false) {
            return Result.error("-1", "请导入excel文件");
        } else {
            try {
                InputStream inputStream = file.getInputStream();
                List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
                System.out.println(lists);
                List<User_info> saveList = new ArrayList<>();
                for (List<Object> row : lists) {
                    User_info user_info = new User_info();
                    user_info.setID(row.get(0).toString());
                    user_info.setUname(row.get(1).toString());
                    user_info.setSex(row.get(2).toString());
                    user_info.setPortrait(row.get(3).toString());
                    user_info.setTelephone(row.get(4).toString());
                    user_info.setAddress(row.get(5).toString());
                    user_info.setPsd(row.get(0).toString());
                    //user_info.setExpenses(Double.parseDouble(row.get(7).toString()));

                    saveList.add(user_info);
                }
                for (User_info user_info : saveList) {
                    User_info user=user_infoMapper.selectById(user_info.getID());
                    if (user_info.getID() != null&&user==null) {
                        user_infoMapper.insert(user_info);
                        success = success + 1;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return Result.success(success);
    }
}

