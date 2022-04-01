package com.example.bucao_springboot.controller;
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

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rfid_kinds")
public class rfid_kindsController {
    @Resource   //将rfid_kindsMapper引入到bucao_infoController中
    RFid_kindsMapper rfid_kindsMapper;

    //新增接口
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
            return Result.error("-1","系统错误，请稍后重试");
        }

    }
    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody RFid_kinds rfid_kinds)
    {

        rfid_kindsMapper.updateById(rfid_kinds);
        return Result.success();

    }

    //删除接口
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id)
    {
        rfid_kindsMapper.deleteById(id);
        System.out.println(id);
        return Result.success();
    }

    //分页查询
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
    //与bucao_info表交互的接口
    @GetMapping("/bucaoinfo")
    public List<Map<String, Object>>  SeletTobucaoinfo()
    {
        QueryWrapper<RFid_kinds> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=rfid_kindsMapper.selectMaps(queryWrapper);
        return list;
    }


    @GetMapping("/exportdata")
    public void exportOrderXlsx(HttpServletResponse response) throws IOException {
        // 先查询所有的数据
        List<RFid_kinds> allrfid_kinds = rfid_kindsMapper.selectList(null);
        System.out.println(allrfid_kinds);

        //return Result.success(loopQuery(null, allCategories));
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //创建xlsx格式的
        //ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("RFNO", "序列号");
        writer.addHeaderAlias("kind", "布草类型");
        writer.addHeaderAlias("stock", "库存");
        writer.addHeaderAlias("section", "所属部门");
        writer.addHeaderAlias("note", "备注");
        //设置单元格宽度
        int[] arr = {30, 30, 25, 10,30};
        for (int i = 0; i < arr.length; i++) {
            writer.setColumnWidth(i, arr[i]);
        }
        writer.write(allrfid_kinds, true);
        // 合并单元格后的标题行，使用默认标题样式，从0开始
        writer.merge(4, "RFID");
        //只导出有别名的字段
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(allrfid_kinds, true);
        String excelName = "RFID";
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes("utf-8"), "ISO-8859-1") + ".xls");
        ServletOutputStream out=response.getOutputStream();

        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }


}
