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
import com.example.bucao_springboot.entity.Section;
import com.example.bucao_springboot.entity.Section;
import com.example.bucao_springboot.mapper.SectionMapper;
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
@RequestMapping("/Section")
@Api(tags = "部门信息管理")
public class SectionController {
    @Resource
    SectionMapper sectionMapper;

    /**与Bucao_info表交互的接口：查询出布草所属所有部门
     *
     * @return
     */
    @GetMapping("/rfid_kinds")
    @ApiOperation(value = "查询所有部门接口",notes="与Bucao_info表交互的接口：查询出布草所属所有部门")
    public List<Map<String, Object>> SeletToRfidKinds()
    {
        QueryWrapper<Section> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=sectionMapper.selectMaps(queryWrapper);
        return list;
    }

    /**新增接口
     *
     * @param section
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增接口",notes="新增，参数：部门实体类")
    public Result<?> save(@RequestBody Section section)
    {
        try {
            Section user=sectionMapper.selectOne(Wrappers.<Section>lambdaQuery().eq(Section::getId,section.getId()));
            if(user==null) {
                sectionMapper.insert(section);
                System.out.println("Section已添加部门"+section.getId()+"信息：");
                return Result.success();
            }
            else
            {
                return Result.error("-1","该部门已存在");
            }
        }catch (Exception e)
        {
            return Result.error("-1","系统后台出错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param Section
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新接口",notes="参数：部门实体类")
    public Result<?> update(@RequestBody Section Section)
    {
        try {
            sectionMapper.updateById(Section);
            System.out.println("Section已更新部门" + Section.getId() + "的信息：");
            return Result.success();
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","请先删除布草信息基本表、病房表、RFID标签分类表中的相关记录");
        }
    }

    //删除接口
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除接口",notes="根据id删除")
    public Result<?> delete(@PathVariable String id)
    {
        try {
            sectionMapper.deleteById(id);
            System.out.println("Section已删除部门" + id + "的信息：");
            return Result.success();
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return  Result.error("-1","请先删除布草信息基本表、病房表、RFID标签分类表中的相关记录");
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
    @ApiOperation(value = "分页查询接口",notes="分页查询接口")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        // Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        // LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        LambdaQueryWrapper<Section> wrapper = Wrappers.<Section>lambdaQuery();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(Section::getId,search);//eq(a,b)<=>a=b
        }
        Page<Section> Section_page=sectionMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);

        return Result.success(Section_page);
    }

    /**显示部门信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询部门",notes="根据id查询部门信息")
    public Result<?> SelectPerson_Info(@PathVariable String id)
    {
        sectionMapper.selectById(id);
        System.out.println("Section已查询到部门"+id+"的信息：");
        return Result.success();
    }


    /**批量删除接口
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除接口",notes="根据id的集合删除")
    public Result<?> deleteBatch(@RequestBody List<String> ids) {

        sectionMapper.deleteBatchIds(ids);
        return Result.success();
    }

    /**
     * Excel导出
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    @ApiOperation(value = "excel文件导出接口",notes="excel文件导出")
    public void export(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Section> all = sectionMapper.selectList(null);
        for (Section Section : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("编号", Section.getId());
            row1.put("名称", Section.getNa());
            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("部门信息数据表", "UTF-8");
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
    @ApiOperation(value = "excel文件导入接口",notes="excel文件导入")
    public Result<?> upload(@ApiParam(value = "选择excel文件") @Valid @RequestPart(value = "file") MultipartFile file) throws IOException {
        Integer num = 0;//统计导入成功的记录条数
        try {
            InputStream inputStream = file.getInputStream();
            List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
            System.out.println(lists);
            List<Section> saveList = new ArrayList<>();
            for (List<Object> row : lists) {
                Section Section = new Section();
                Section.setId(row.get(0).toString());
                Section.setNa(row.get(1).toString());
                saveList.add(Section);
            }

            for (Section Section : saveList) {

                if (Section.getId() != null) {
                    sectionMapper.insert(Section);
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
