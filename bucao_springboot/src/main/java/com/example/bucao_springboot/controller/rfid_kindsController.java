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
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.mapper.RFid_kindsMapper;

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
@RequestMapping("/rfid_kinds")
public class rfid_kindsController {
    @Resource   //将rfid_kindsMapper引入到bucao_infoController中
    RFid_kindsMapper rfid_kindsMapper;

    /**新增接口
     *
     * @param rfid_kinds
     * @return
     */
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
    public Result<?> update(@RequestBody RFid_kinds rfid_kinds)
    {
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
    public Result<?> SelectRFID_Info(@PathVariable String id)
    {
        RFid_kinds rfid = rfid_kindsMapper.selectById(id);
        System.out.println("rfid_kinds已查询到布草类别"+id+"的信息：");
        return Result.success(rfid);
    }

    /**删除接口
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id)
    {
        System.out.println(id);
        try {
            rfid_kindsMapper.deleteById(id);
            return Result.success();
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
    @GetMapping("/insertall")
    public Result<?> Addall(@RequestBody List< RFid_kinds> rfid_kinds){

        for(RFid_kinds rf:rfid_kinds)
        {
            if(rf.getRFNO()!=null)
            {
                rfid_kindsMapper.insert(rf);
            }
        }
        return Result.success( rfid_kindsMapper);
    }

    /**查询各种布草的库存
     *
     * @return
     */
    @GetMapping("/kinds_stocks")
    public Result<?>  kinds_stocks(){
        return Result.success(rfid_kindsMapper.STOCKS());
    }

    /**查询各种布草的库存
     *
     * @return
     */
    @GetMapping("/section_stocks")
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

    /**与bucao_info表交互的接口
     *
     * @return
     */
    @GetMapping("/bucaoinfo")
    public List<Map<String, Object>>  SeletTobucaoinfo()
    {
        QueryWrapper<RFid_kinds> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=rfid_kindsMapper.selectMaps(queryWrapper);
        return list;
    }

    /**批量删除接口
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<String> ids) {
        rfid_kindsMapper.deleteBatchIds(ids);
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
        Integer num=0;//统计导入成功的记录条数
        for (RFid_kinds rfid_kinds : saveList) {
            if(rfid_kinds.getRFNO()!=null) {
                rfid_kindsMapper.insert(rfid_kinds);
                num=num+1;
            }
        }
        return Result.success();
    }
}
