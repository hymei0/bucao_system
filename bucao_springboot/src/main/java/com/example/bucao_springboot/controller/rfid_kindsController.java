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
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.mapper.RFid_kindsMapper;

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
@RequestMapping("/rfid_kinds")
@Api(tags = "RFID标签分类信息管理")
public class rfid_kindsController {
    @Resource   //将rfid_kindsMapper引入到bucao_infoController中
    RFid_kindsMapper rfid_kindsMapper;

    /** 新增接口
     *
     * @param rfid_kinds
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增接口",notes="新增接口")
    public Result<?> save(@RequestBody RFid_kinds rfid_kinds)
    {
        try{
            RFid_kinds result=rfid_kindsMapper.selectOne(Wrappers.<RFid_kinds>lambdaQuery().eq(RFid_kinds::getRFNO,rfid_kinds.getRFNO()));
            if(result==null) {
                rfid_kindsMapper.insert(rfid_kinds);
                System.out.println("RFid_kinds已添加布草类别"+rfid_kinds.getRFNO()+"的信息：");
                return Result.success();
            }
            else{
                return Result.error("-1","该类布草已经存在");
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }

    }

    /**更新接口
     *
     * @param rfid_kinds
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新接口",notes="更新接口")
    public Result<?> update(@RequestBody RFid_kinds rfid_kinds)
    {
        RFid_kinds rf=rfid_kindsMapper.selectOne(Wrappers.<RFid_kinds>lambdaQuery().eq(RFid_kinds::getRFNO,rfid_kinds.getRFNO()));
        if(rf==null)
        {
            return  Result.error("-1","该记录不存在");
        }
        try {
            rfid_kindsMapper.updateById(rfid_kinds);
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","请先删除布草表中的相关记录再做更新");
        }
    }

    /**通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过id查询接口",notes="参数: 通过id查询")
    public Result<?> SelectRFID_Info(@PathVariable String id)
    {
        RFid_kinds rfid = rfid_kindsMapper.selectById(id);
        if(rfid!=null) {
            System.out.println("rfid_kinds已查询到布草类别" + id + "的信息：");
            return Result.success(rfid);
        }else {
            return Result.error("-1","没有查找到相关记录");
        }
    }

    /**删除接口
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除接口",notes="根据id删除")
    public Result<?> delete(@PathVariable String id)
    {

        try {
            RFid_kinds rfid = rfid_kindsMapper.selectById(id);
            if(rfid!=null) {
                rfid_kindsMapper.deleteById(id);
                return Result.success();
            }else {
                return Result.error("-1","没有查找到相关记录");
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","请先删除布草基本信息表中的相关记录");
        }
    }

    /**批量插入
     *
     * @param rfid_kinds
     * @return
     */
    @PostMapping("/insertall")
    @ApiOperation(value = "批量插入接口",notes="参数: RFid标签分类表实体类列表")
    public Result<?> Addall(@RequestBody List< RFid_kinds> rfid_kinds){
        Integer sucess=0;
        try {
            for (RFid_kinds rf : rfid_kinds) {
                if (rf.getRFNO() != null) {
                    rfid_kindsMapper.insert(rf);
                    sucess=sucess+1;
                }
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return Result.success(sucess);
    }

    /**查询各种布草的库存
     *
     * @return
     */
    @GetMapping("/kinds_stocks")
    @ApiOperation(value = "查询各种布草的库存接口",notes="查询各种布草的库存")
    public Result<?>  kinds_stocks(){
        return Result.success(rfid_kindsMapper.STOCKS());
    }

    /**查询各部门布草的库存
     *
     * @return
     */
    @GetMapping("/section_stocks")
    @ApiOperation(value = "查询各部门布草的库存接口",notes="查询各部门布草的库存")
    public Result<?>  kinds_section(){
        return Result.success(rfid_kindsMapper.STOCKS_setion());
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
        //Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

         //LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));
         if(pageNum<1){
             return Result.error("-1","pageNum不能小于1");
         }
         if(pageSize<1){
             return Result.error("-1","pageSize不能小于1");
         }
         LambdaQueryWrapper<RFid_kinds> wrapper = Wrappers.<RFid_kinds>lambdaQuery();
         if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
         {
             wrapper.like(RFid_kinds::getRFNO,search).or().like(RFid_kinds::getKind,search).or().like(RFid_kinds::getSection,search).or().like(RFid_kinds::getNote,search).or().like(RFid_kinds::getStock,search);//eq(a,b)<=>a=b
         }
         Page<RFid_kinds> rfid_kinds_page=rfid_kindsMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(rfid_kinds_page);
    }

    /**与bucao_info表交互的接口
     *
     * @return
     */
    @GetMapping("/bucaoinfo")
    @ApiOperation(value = "查询所有记录",notes="与bucao_info表交互的接口")
    public List<Map<String, Object>>  SeletTobucaoinfo()
    {
        try {
            QueryWrapper<RFid_kinds> queryWrapper = new QueryWrapper<>();
            List<Map<String, Object>> list = rfid_kindsMapper.selectMaps(queryWrapper);
            return list;
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }

    /**批量删除接口
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除接口",notes="根据id集合批量删除")
    public Result<?> deleteBatch(@RequestBody List<String> ids) {
        Integer suncess=0;
        try {
            for (int i = 0; i < ids.size(); i++) {
                if (rfid_kindsMapper.selectById(ids.get(i)) != null&&rfid_kindsMapper.selectUniqueRFID(ids.get(i)).size()>0) {
                    rfid_kindsMapper.deleteById(ids.get(i));
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
    @ApiOperation(value = "excel文件导出接口",notes="excel文件导出")
    public Result<?> export(HttpServletResponse response) throws IOException {

        try {

            List<Map<String, Object>> list = CollUtil.newArrayList();

            List<RFid_kinds> all = rfid_kindsMapper.selectList(null);
            for (RFid_kinds RFid_kinds : all) {
                Map<String, Object> row1 = new LinkedHashMap<>();
                row1.put("序列号", RFid_kinds.getRFNO());
                row1.put("布草类型", RFid_kinds.getKind());
                row1.put("库存", RFid_kinds.getStock());
                row1.put("所属部门", RFid_kinds.getSection());
                row1.put("备注", RFid_kinds.getNote());
                list.add(row1);
            }

            // 2. 写excel
            ExcelWriter writer = ExcelUtil.getWriter(true);
            writer.write(list, true);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String fileName = URLEncoder.encode("RFID标签分类数据表", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            writer.close();
            IoUtil.close(System.out);
            return Result.success("导出成功");
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
        Integer num = 0;//统计导入成功的记录条数
        excelconfig ex = new excelconfig();
        if (ex.getFileType(file.getOriginalFilename())==false) {
            return Result.error("-1", "请导入excel文件");
        } else {
            try {
                InputStream inputStream = file.getInputStream();
                List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
                List<RFid_kinds> saveList = new ArrayList<>();
                for (List<Object> row : lists) {

                    RFid_kinds rfid_kinds = new RFid_kinds();
                    rfid_kinds.setRFNO(row.get(0).toString());
                    rfid_kinds.setKind(row.get(1).toString());
                    rfid_kinds.setStock(Integer.valueOf((row.get(2).toString())));
                    rfid_kinds.setSection(row.get(3).toString());
                    rfid_kinds.setNote(row.get(4).toString());

                    saveList.add(rfid_kinds);
                }

                for (RFid_kinds rfid_kinds : saveList) {
                    RFid_kinds rf=rfid_kindsMapper.selectById(rfid_kinds.getRFNO());
                    if (rfid_kinds.getRFNO() != null&&rf==null) {
                        rfid_kindsMapper.insert(rfid_kinds);
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
