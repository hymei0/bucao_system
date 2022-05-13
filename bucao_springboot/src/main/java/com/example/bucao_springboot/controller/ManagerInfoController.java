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
import com.example.bucao_springboot.entity.ManagerInfo;
import com.example.bucao_springboot.entity.ManagerInfo;
import com.example.bucao_springboot.mapper.ManagerInfoMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/ManagerInfo")
@Api(tags = "管理员信息管理")
public class ManagerInfoController {
    @Resource
    ManagerInfoMapper ManagerInfoMapper;

    /**登录接口
     *
     * @param managerInfo
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录接口",notes="登录接口")
    public Result<?> login(@RequestBody ManagerInfo managerInfo)
    {

        ManagerInfo res=ManagerInfoMapper.selectOne(Wrappers.<ManagerInfo>lambdaQuery().eq(ManagerInfo::getId,managerInfo.getId()).eq(ManagerInfo::getPsd,managerInfo.getPsd()));

        if(res == null)
        {
            return Result.error("-1","账号或密码错误");
        }
        System.out.println("ManagerInfo已登录到"+managerInfo.getId()+"的账户");
        return Result.success(res);
    }

    /**注册接口
     *
     * @param managerInfo
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册接口",notes="注册接口")
    public Result<?> register(@RequestBody ManagerInfo managerInfo)
    {
        try{
            ManagerInfo user=ManagerInfoMapper.selectOne(Wrappers.<ManagerInfo>lambdaQuery().eq(ManagerInfo::getId,managerInfo.getId()).or().eq(ManagerInfo::getTelephone,managerInfo.getTelephone()));
            if(user==null) {
                ManagerInfoMapper.insert(managerInfo);
                System.out.println("ManagerInfo已注册管理员"+managerInfo.getId()+"的信息：");
                return Result.success();
            }
            else{
                return Result.error("-1","该账号/或该电话已存在");
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","系统错误，请稍后重试");
        }
    }

    /**无条件查询
     *
     * @return
     */
    @GetMapping("/selectall")
    @ApiOperation(value = "查询所有管理员",notes="无条件查询")
    public Result<?>  selectall(){
        QueryWrapper<ManagerInfo> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=ManagerInfoMapper.selectMaps(queryWrapper);
        return Result.success(list);
    }

    /**新增接口
     *
     * @param managerInfo
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增接口",notes="新增接口")
    public Result<?> save(@RequestBody ManagerInfo managerInfo)
    {
        try {
            ManagerInfo user=ManagerInfoMapper.selectOne(Wrappers.<ManagerInfo>lambdaQuery().eq(ManagerInfo::getId,managerInfo.getId()).or().eq(ManagerInfo::getTelephone,managerInfo.getTelephone()));
            if(user==null) {
                managerInfo.setPsd(managerInfo.getId());
                ManagerInfoMapper.insert(managerInfo);
                System.out.println("ManagerInfo已添加管理员"+managerInfo.getId()+"信息：");
                return Result.success();
            }
            else
            {
                return Result.error("-1","账号或号码已存在");
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","系统后台出错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param managerInfo
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新接口",notes="更新接口")
    public Result<?> update(@RequestBody ManagerInfo managerInfo)
    {
        ManagerInfo user=ManagerInfoMapper.selectOne(Wrappers.<ManagerInfo>lambdaQuery().eq(ManagerInfo::getId,managerInfo.getId()).or().eq(ManagerInfo::getTelephone,managerInfo.getTelephone()));
        if(user==null)
        {
            return  Result.error("-1","该管理员账号不存在");
        }
        ManagerInfoMapper.updateById(managerInfo);
         System.out.println("ManagerInfo已更新管理员"+managerInfo.getId()+"的信息：");
         return Result.success();
    }

    /**删除接口
     *
     * @param Id
     * @return
     */
    @DeleteMapping("/{Id}")
    @ApiOperation(value = "删除接口",notes="删除接口")
    public Result<?> delete(@PathVariable String Id)
    {
        ManagerInfo user=ManagerInfoMapper.selectOne(Wrappers.<ManagerInfo>lambdaQuery().eq(ManagerInfo::getId,Id));
        if(user==null)
        {
            return  Result.error("-1","该管理员账号不存在");
        }
        ManagerInfoMapper.deleteById(Id);
        System.out.println("ManagerInfo已删除管理员"+Id+"的信息：");
        return Result.success();
    }

    /**分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询接口",notes="分页查询接口")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        // Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        // LambdaQueryWrapper<RFId_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        if(pageNum<1){
            return Result.error("-1","pageNum不能小于1");
        }
        if(pageSize<1){
            return Result.error("-1","pageSize不能小于1");
        }
        try {
           LambdaQueryWrapper<ManagerInfo> wrapper = Wrappers.<ManagerInfo>lambdaQuery();
           if (StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
           {
               // wrapper.like(ManagerInfo::getId, search);//eq(a,b)<=>a=b
               wrapper.like(ManagerInfo::getId,search).or().like(ManagerInfo::getTelephone,search).or().like(ManagerInfo::getAddress,search).or().like(ManagerInfo::getSex,search).or().like(ManagerInfo::getMname,search).or().like(ManagerInfo::getEmail,search).or().like(ManagerInfo::getPri,search);
           }
           Page<ManagerInfo> ManagerInfo_page = ManagerInfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

           return Result.success(ManagerInfo_page);
       }catch (Exception e){
           System.out.println(e.toString());
           return Result.error("-1","分页查询失败");
       }
    }

    /**显示个人信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "显示个人信息接口",notes="根据id查询管理员个人信息")
    public Result<?> SelectPerson_Info(@PathVariable String id)
    {
        ManagerInfo user=ManagerInfoMapper.selectById(id);
        if(user==null)
        {
            return Result.error("-1","该管理员不存在");
        }
        else{
            System.out.println("ManagerInfo已查询到管理员"+id+"的信息");
            return Result.success(user);
        }
    }


    /**批量删除接口
     *
     * @param Ids
     * @return
     */

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除接口",notes="根据id集合批量删除")
    public Result<?> deleteBatch(@RequestBody List<String> Ids) {
        Integer suncess=0;
        try {
            for (int i = 0; i < Ids.size(); i++) {
                if (ManagerInfoMapper.selectById(Ids.get(i)) != null) {
                    ManagerInfoMapper.deleteById(Ids.get(i));
                    suncess++;
                }
            }
        }catch (Exception e)
        {
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
    @ApiOperation(value = "excel导出接口",notes="excel导出接口")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<ManagerInfo> all = ManagerInfoMapper.selectList(null);
        for (ManagerInfo ManagerInfo : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("账号", ManagerInfo.getId());
            row1.put("姓名", ManagerInfo.getMname());
            row1.put("性别", ManagerInfo.getSex());
            row1.put("权限",ManagerInfo.getPri());
            row1.put("头像", ManagerInfo.getPortrait());
            row1.put("联系电话", ManagerInfo.getTelephone());
            row1.put("地址", ManagerInfo.getAddress());
            row1.put("邮箱",ManagerInfo.getEmail());
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
    @ApiOperation(value = "excel导入接口",notes="excel导入接口")
    public Result<?> upload(@ApiParam(value = "选择excel文件") @Valid @RequestPart(value = "file") MultipartFile file) throws IOException {
        Integer num = 0;//统计导入成功的记录条数
        excelconfig ex = new excelconfig();
        if (ex.getFileType(file.getOriginalFilename())==false) {
            return Result.error("-1", "请导入excel文件");
        } else {
            try {
                InputStream inputStream = file.getInputStream();
                List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
                List<ManagerInfo> saveList = new ArrayList<>();
                for (List<Object> row : lists) {
                    ManagerInfo ManagerInfo = new ManagerInfo();
                    ManagerInfo.setId(row.get(0).toString());
                    ManagerInfo.setMname(row.get(1).toString());
                    ManagerInfo.setSex(row.get(2).toString());
                    ManagerInfo.setPri(row.get(3).toString());
                    ManagerInfo.setPortrait(row.get(4).toString());
                    ManagerInfo.setTelephone(row.get(5).toString());
                    ManagerInfo.setAddress(row.get(6).toString());
                    ManagerInfo.setEmail(row.get(7).toString());
                    ManagerInfo.setPsd(row.get(0).toString());
                    //ManagerInfo.setExpenses(Double.parseDouble(row.get(7).toString()));

                    saveList.add(ManagerInfo);
                }
                for (ManagerInfo ManagerInfo : saveList) {
                    ManagerInfo manager=ManagerInfoMapper.selectById(ManagerInfo.getId());
                    if (ManagerInfo.getId() != null&&manager==null) {
                        ManagerInfoMapper.insert(ManagerInfo);
                        num = num + 1;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return Result.success(num);
    }
}

