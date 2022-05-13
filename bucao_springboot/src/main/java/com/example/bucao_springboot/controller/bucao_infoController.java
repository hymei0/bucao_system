package com.example.bucao_springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.XML;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.common.excelconfig;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.mapper.Bucao_infoMapper;
import com.example.bucao_springboot.mapper.RFid_kindsMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.*;

@RestController
@RequestMapping("/Bucao_info")
@Api(tags = "布草信息管理")
public class bucao_infoController {
    //将Bucao_infoMapper引入到bucao_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_infoMapper bucao_infoMapper;
    @Resource
    RFid_kindsMapper RFid_kindsMapper;

    /**新增接口
     *
     * @param bucao_info
     * @return
     */
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成Bucao_info类
    @ApiOperation(value = "新增接口",notes="新增")
    public Result<?> save(@RequestBody @Valid Bucao_info bucao_info)
    {

        try{
            QueryWrapper<Bucao_info> wrapper=new QueryWrapper<>();
            wrapper.eq("rfno", bucao_info.getRfno()).eq("rfid", bucao_info.getRfid());
            Bucao_info result=bucao_infoMapper.selectOne(wrapper);
            if(result==null) {
                if(bucao_info.getWashtimes()<0){
                    return  Result.error("-1","洗涤次数必须大于0");
                }else {
                    //测试RFNO是否存在在RFID标签分类表中
                    if(RFid_kindsMapper.selectById(bucao_info.getRfno())!=null)
                    {
                        bucao_infoMapper.insert(bucao_info);
                        return Result.success();
                    }else{
                        return Result.error("-1","RFID标签分类表中不存在"+bucao_info.getRfno()+"类布草");
                    }
                }
            }
            else{
                return Result.error("-1","该布草已经存在");
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param bucao_info
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新接口",notes="更新病房")
    public Result<?> update(@RequestBody @Valid  Bucao_info bucao_info)
    {

        try {
            QueryWrapper<Bucao_info> wrapper=new QueryWrapper<>();
            wrapper.eq("rfno", bucao_info.getRfno()).eq("rfid", bucao_info.getRfid());
            Bucao_info result=bucao_infoMapper.selectOne(wrapper);
            if(result==null){
                return Result.error("-1","该布草不存在");
            }
            else {
                if(bucao_info.getWashtimes()<0){
                    return  Result.error("-1","洗涤次数必须大于0");
                }
                bucao_infoMapper.update(bucao_info, Wrappers.<Bucao_info>lambdaUpdate().eq(Bucao_info::getRfno, bucao_info.getRfno()).eq(Bucao_info::getRfid, bucao_info.getRfid()));
                return Result.success();
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }

    }

    /**删除接口
     *
     * @param rfno
     * @param rfid
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除接口",notes="根据复合主键删除")
    public Result<?> delete(@RequestParam String rfno,
                            @RequestParam String rfid)
    {
        try {
            // Bucao_info bucao=bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,bucao_info.getRfno()).eq(Bucao_info::getRfid,bucao_info.getRfid()));
            QueryWrapper<Bucao_info> wrapper = new QueryWrapper<>();
            wrapper.eq("rfno", rfno).eq("rfid", rfid);
            if(bucao_infoMapper.selectOne(wrapper)==null)
            {
                return Result.error("-1","RFID编号为"+rfno+rfid+"的布草不存在");
            }
            else {
                int rows = bucao_infoMapper.delete(wrapper);
                return Result.success();
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","请先删除布草-用户、布草-病房信息表的相关记录");
        }
    }


    /**查询出处于闲置状态的布草
     *
     * @return
     */
    @GetMapping("/selectall")
    @ApiOperation(value = "查询出处于闲置状态的布草接口",notes="查询出处于闲置状态的布草")
    public List<Map<String, Object>>  selectall(){
        //QueryWrapper<Bucao_info> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=bucao_infoMapper.selectMaps(Wrappers.<Bucao_info>lambdaUpdate().like(Bucao_info::getState,"闲置"));
        return list;
    }

    /**查询布草类型为病号服且状态为闲置或者入库的布草
     *
     * @param rfid
     * @return
     */
    @GetMapping("/foruser")
    @ApiOperation(value = "查询布草类型为'rfid'且状态为闲置的布草接口",notes="查询布草类型为'rfid'且状态为闲置的布草")
    public Result<?>  selectforuser(@RequestParam String rfid){
        List<Map<String, Object>> list=bucao_infoMapper.selectbucaoforuser(rfid);
        return Result.success(list);
    }


    /**入库数据统计
     *
     * @return
     */
    @GetMapping("/indata")
    @ApiOperation(value = "布草入库数据统计接口",notes="布草入库数据统计")
    public Result<?>  indata(){
        //查询数据库中所有数据月份和数据
        return Result.success(bucao_infoMapper.Indata());
    }

    /**出库数据统计
     *
     * @return
     */
    @GetMapping("/outdata")
    @ApiOperation(value = "布草出库是据统计接口",notes="布草出库数据统计")
    public Result<?>  echarts(){
        //查询数据库中所有数据月份和数据
        return Result.success(bucao_infoMapper.Outdata());
    }

    /**复合主键，根据复合id查询
     *
     * @param rfno
     * @param rfid
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation(value = "根据复合id查询接口",notes="根据复合id查询")
    public Result<?> detail(@RequestParam String rfno,
                            @RequestParam String rfid)
    {
        try {
            // Bucao_info bucao=bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,bucao_info.getRfno()).eq(Bucao_info::getRfid,bucao_info.getRfid()));
            QueryWrapper<Bucao_info> wrapper = new QueryWrapper<>();

            wrapper.eq("rfno", rfno).eq("rfid", rfid);
            Bucao_info bucao_info = bucao_infoMapper.selectOne(wrapper);
            if(bucao_info==null){
                return Result.error("1","查询成功，RFID编号为"+rfno+rfid+"的布草不存在");
            }
            return Result.success(bucao_info);
        }catch (Exception e){
            return Result.error("-1","后台出错了");
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
    @ApiOperation(value = "分页查询接口",notes="分页查询")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {

        if(pageNum<1){
            return Result.error("-1","pageNum不能小于1");
        }
        if(pageSize<1){
            return Result.error("-1","pageSize不能小于1");
        }
        QueryWrapper<Bucao_info> wrapper=new QueryWrapper<>();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like("rfid",search).or().like("rfno",search).or().like("state",search);//eq(a,b)<=>a=b
        }
        Page<Bucao_info> bucao_info_page=bucao_infoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(bucao_info_page);
    }
    /**批量删除接口:复合主键
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除接口",notes="根据复合id集合批量删除,返回批删除成功的数据个数")
    public Result<?> deleteBatch(@RequestBody List<List<String>> ids) {
        Integer success=0;
        try {
            for (List<String> id : ids) {

                QueryWrapper<Bucao_info> wrapper = new QueryWrapper<>();
                wrapper.eq("rfno", id.get(0)).eq("rfid", id.get(1));
                if (bucao_infoMapper.selectOne(wrapper) != null) {
                    bucao_infoMapper.delete(wrapper);
                    success++;
                }

            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }
        System.out.println("一共成功删除"+success.toString()+"条记录");
        return Result.success(success);
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

        List<Bucao_info> all = bucao_infoMapper.selectList(null);
        for (Bucao_info bucao_info : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("布草类型", bucao_info.getRfno());
            row1.put("RFID编码", bucao_info.getRfid());
            row1.put("布草状态", bucao_info.getState());
            row1.put("洗涤次数", bucao_info.getWashtimes());
            row1.put("入库时间", DateUtil.format(bucao_info.getIndate(),"yyyy-MM-dd"));
            row1.put("报废时间", DateUtil.format(bucao_info.getOutdate(),"yyyy-MM-dd"));

            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("布草信息数据表", "UTF-8");
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
            Integer success = 0;
            //file.getName()

            InputStream inputStream = file.getInputStream();
            excelconfig ex = new excelconfig();
            if (ex.getFileType(file.getOriginalFilename())==false) {
                return Result.error("-1", "请导入excel文件");
            } else {
                try {
                    List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
                    List<Bucao_info> saveList = new ArrayList<>();
                    for (List<Object> row : lists) {
                        Bucao_info bucao_info = new Bucao_info();
                        bucao_info.setRfno(row.get(0).toString());
                        bucao_info.setRfid(row.get(1).toString());
                        bucao_info.setState(row.get(2).toString());
                        bucao_info.setWashtimes(Integer.valueOf((row.get(3).toString())));
                        bucao_info.setIndate(Date.valueOf(DateUtil.format(DateUtil.parse(row.get(4).toString()), "yyyy-MM-dd")));
                        if (row.size() >= 6) {
                            if (!row.get(5).equals("")) {
                                bucao_info.setOutdate(Date.valueOf(DateUtil.format(DateUtil.parse(row.get(5).toString()), "yyyy-MM-dd")));
                            } else {
                                bucao_info.setOutdate(null);
                            }
                        } else {
                            bucao_info.setOutdate(null);
                        }
                        saveList.add(bucao_info);
                    }

                    for (Bucao_info bucao_info : saveList) {
                        Bucao_info bucao=bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfid,bucao_info.getRfid()).eq(Bucao_info::getRfno,bucao_info.getRfno()));
                        if (bucao==null&&bucao_infoMapper.isValid(bucao_info.getRfno(), bucao_info.getRfid()) != null && bucao_info.getRfid() != null) {
                            bucao_infoMapper.insert(bucao_info);
                            System.out.println(bucao_info + "导入成功");
                            success = success + 1;
                        }
                    }
                }catch(Exception e)
                {
                    System.out.println(e.toString());
                }
                return Result.success(success);
            }
    }
}




