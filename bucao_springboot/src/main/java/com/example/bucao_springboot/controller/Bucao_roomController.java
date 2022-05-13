package com.example.bucao_springboot.controller;

import ch.qos.logback.core.util.FileUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.common.excelconfig;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.entity.Bucao_room;
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.mapper.*;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Bucao_room")
@Api(tags = "布草-病房管理")
public class Bucao_roomController {
    //将xxx_infoMapper引入到xxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_roomMapper bucao_roomMapper;
    @Resource
    Bucao_userMapper bucao_userMapper;
    @Resource
    Bucao_infoMapper Bucao_infoMapper;
    @Resource
    Room_infoMapper Room_infoMapper;
    @Resource
    RFid_kindsMapper RFid_kindsMapper;



    /**新增接口
     *
     * @param Bucao_room
     * @return
     */
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成xxx类
    @ApiOperation(value = "新增接口",notes="新增")
    public Result<?> save(@RequestBody Bucao_room Bucao_room)
    {
        if(Room_infoMapper.selectById(Bucao_room.getRoomId())==null)
        {
            return Result.error("-1","病房信息中并未找到编号为"+Bucao_room.getRoomId()+"的病房");
        }
        if(Bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,Bucao_room.getRfno()).eq(Bucao_info::getRfid,Bucao_room.getRfid()))==null)
        {
            return Result.error("-1","布草信息中并未找到RFID编号为"+Bucao_room.getRfno()+Bucao_room.getRfid()+"的布草");
        }
        if(!RFid_kindsMapper.selectById(Bucao_room.getRfno()).getSection().equals(Bucao_room.getBucaoSection()))
        {
            return Result.error("-1","布草"+Bucao_room.getRfno()+Bucao_room.getRfid()+"应属于"+RFid_kindsMapper.selectById(Bucao_room.getRfno()).getSection());
        }
        if(!Room_infoMapper.selectById(Bucao_room.getRoomId()).getSection().equals(Bucao_room.getRoomSection()))
        {
            return Result.error("-1","病房"+Bucao_room.getRoomId()+"应属于"+Room_infoMapper.selectById(Bucao_room.getRoomId()).getSection());
        }
        try{
            QueryWrapper<Bucao_room> wrapper=new QueryWrapper<>();
            wrapper.eq("rfno", Bucao_room.getRfno()).eq("rfid", Bucao_room.getRfid()).eq("room_id",Bucao_room.getRoomId());
            Bucao_room result=bucao_roomMapper.selectOne(wrapper);
            if(result==null) {
                bucao_roomMapper.insert(Bucao_room);
                return Result.success();
            }
            else{
                return Result.error("-1","该记录已存在");
            }
        }catch (Exception e){
            return Result.error("-1","后台错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param bucao_room
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新接口",notes="更新")
    public Result<?> update(@RequestBody Bucao_room bucao_room)
    {
        try {
            bucao_roomMapper.update(bucao_room, Wrappers.<Bucao_room>lambdaUpdate().eq(Bucao_room::getRfno, bucao_room.getRfno()).eq(Bucao_room::getRfid, bucao_room.getRfid()).eq(Bucao_room::getRoomId, bucao_room.getRoomId()));
            return Result.success();
        }catch (Exception e){
            return Result.error("-1","后台出错啦，请联系开发人员");
        }

    }

    /** 查询未使用的病房
     *
     * @return
     */
    @GetMapping("/rooms")
    @ApiOperation(value = "查询未住满的病房接口",notes="查询未住满的病房")
    public Result<?>rooms()
    {
        List<Map<String, Object>> list=bucao_roomMapper.getrooms();
        return Result.success(list);
    }

    /**删除接口
     *
     * @param roomId
     * @param rfno
     * @param rfid
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除接口",notes="删除布草病房分配记录")
    public Result<?> delete(@RequestParam String roomId,
                            @RequestParam String rfno,
                            @RequestParam String rfid)
    {

        try {
            // Bucao_room bucao=Bucao_roomMapper.selectOne(Wrappers.<Bucao_room>lambdaQuery().eq(Bucao_room::getRfno,Bucao_room.getRfno()).eq(Bucao_room::getRfid,Bucao_room.getRfid()));
            QueryWrapper<Bucao_room> wrapper = new QueryWrapper<>();

            wrapper.eq("rfno", rfno).eq("rfid", rfid).eq("room_id",roomId);
            if(bucao_roomMapper.selectOne(wrapper)==null)
            {
                return Result.error("-1","该记录不存在");
            }else {
                bucao_userMapper.updatebucao(rfno, rfid, "已回收"); //归还布草后布草的状态变为已回收
                int rows = bucao_roomMapper.delete(wrapper);
                System.out.println(roomId + "," + rfid + rfno + "该记录删除成功");
                return Result.success();
            }
        }catch (Exception e){
            return Result.error("-1","后台出错啦，请联系开发人员");
        }
    }

    /**分页查询：只有管理员界面用到
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

        QueryWrapper<Bucao_room> wrapper=new QueryWrapper<>();

        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {

            wrapper.like("rfid",search).or().like("rfno",search).or().like("room_id",search).or().like("bucao_Section",search).or().like("room_Section",search);//eq(a,b)<=>a=b
        }
        Page<Bucao_room> Bucao_room_page=bucao_roomMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        System.out.println(bucao_roomMapper.selectList(wrapper));

        return Result.success(Bucao_room_page);
    }
    /**批量删除接口:复合主键
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除接口",notes="根据复合id批量删除：ids=[rfno,rfid,roomId],返回删除成功的数据个数")
    public Result<?> deleteBatch(@RequestBody List<List<String>> ids) {
        Integer success=0;
        try {
            for (List<String> id : ids) {
                QueryWrapper<Bucao_room> wrapper = new QueryWrapper<>();
                bucao_userMapper.updatebucao(id.get(0), id.get(1), "已回收"); //归还布草后布草的状态变为已回收
                wrapper.eq("rfno", id.get(0)).eq("rfid", id.get(1)).eq("room_id", id.get(2));
                if (bucao_roomMapper.selectOne(wrapper) != null) {
                    bucao_roomMapper.delete(wrapper);
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

        List<Bucao_room> all = bucao_roomMapper.selectList(null);
        for (Bucao_room Bucao_room : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("病房号", Bucao_room.getRoomId());
            row1.put("布草RFID编号", Bucao_room.getRfno()+Bucao_room.getRfid());

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
    @ApiOperation(value = "excel导入接口",notes="excel导入接口")
    public Result<?> upload(@ApiParam(value = "选择excel文件") @Valid @RequestPart(value = "file") MultipartFile file) throws IOException {
        Integer num = 0;//统计导入成功的记录条数
        excelconfig ex = new excelconfig();
//        System.out.println("+++++++"+(double)file.getSize());
//        if((double)file.getSize()>=1048576 )
//        {
//            return Result.error("-1", "文件不能超过1M");
//        }
        if (ex.getFileType(file.getOriginalFilename())==false) {
            return Result.error("-1", "请导入excel文件");
        } else {
            try {

                InputStream inputStream = file.getInputStream();
                List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
                List<Bucao_room> saveList = new ArrayList<>();
                for (List<Object> row : lists) {
                    Bucao_room Bucao_room = new Bucao_room();
                    Bucao_room.setRfno(row.get(0).toString());
                    Bucao_room.setBucaoSection(RFid_kindsMapper.selectById(Bucao_room.getRfno()).getSection());
                    Bucao_room.setRfid(row.get(1).toString());
                    Bucao_room.setRoomId(row.get(2).toString());
                    Bucao_room.setRoomSection(Room_infoMapper.selectById(Bucao_room.getRoomId()).getSection());
                    saveList.add(Bucao_room);
                }
                for (Bucao_room bucao_room : saveList) {
                    Bucao_room br=bucao_roomMapper.selectOne(Wrappers.<Bucao_room>lambdaQuery().eq(Bucao_room::getRoomId,bucao_room.getRoomId()).eq(Bucao_room::getRfid,bucao_room.getRfid()).eq(Bucao_room::getRfno,bucao_room.getRfno()));
                    if (br==null&&bucao_room.getRfid() != null && bucao_room.getRfno() != null && bucao_room.getRoomId()!=null) {
                        bucao_roomMapper.insert(bucao_room);
                        num = num + 1;//统计导入成功的记录条数
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return Result.success(num);
        }
    }

}
