package com.example.bucao_springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.entity.Bucao_user;
import com.example.bucao_springboot.entity.User_room;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Bucao_user")
@Api(tags = "布草-用户管理")
public class Bucao_userController {
    //将xxx_infoMapper引入到xxx_infoController中,不太规范，一般是写service类，controller引入service,service引入mapper
    @Resource
    Bucao_userMapper bucao_userMapper;
    @Resource
    User_infoMapper user_infoMapper;
    @Resource
    Bucao_infoMapper Bucao_infoMapper;
    @Resource
    User_roomMapper User_roomMapper;



    /**新增接口
     *
     * @param bucao_user
     * @return
     */
    @ApiOperation(value = "新增接口",notes="新增")
    @PostMapping  //post接口：前台把json数据传过来，通过此接口接收到  并转化成xxx类
    public Result<?> save(@RequestBody Bucao_user bucao_user)
    {

        if(Bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,bucao_user.getRfno()).eq(Bucao_info::getRfid,bucao_user.getRfid()))==null)
        {
            return Result.error("-1","布草信息中并未找到RFID编号为"+bucao_user.getRfno()+bucao_user.getRfid()+"的布草");
        }

        if(User_roomMapper.selectOne(Wrappers.<User_room>lambdaQuery().eq(User_room::getUserid,bucao_user.getUserId()).eq(User_room::getRoomid,bucao_user.getRoomId()))==null)
        {
            return Result.error("-1","未找到ID为"+bucao_user.getUserId()+"用户住在"+bucao_user.getRoomId()+"号病房的住院信息");
        }
        try{
            QueryWrapper<Bucao_user> wrapper=new QueryWrapper<>();
            wrapper.eq("rfno", bucao_user.getRfno()).eq("rfid", bucao_user.getRfid()).eq("user_id",bucao_user.getUserId());
            Bucao_user result=bucao_userMapper.selectOne(wrapper);
            if(result==null) {
                bucao_userMapper.updatebucao(bucao_user.getRfno(),bucao_user.getRfid(),"使用中");//将用户领取的布草的状态改变为使用中
                bucao_userMapper.insert(bucao_user);

                return Result.success();
            }
            else{
                return Result.error("-1","该记录已存在，如有变更需求请进行修改操作！");
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param bucao_user
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新接口",notes="更新")
    public Result<?> update(@RequestBody Bucao_user bucao_user)
    {
        try {
            bucao_userMapper.update(bucao_user, Wrappers.<Bucao_user>lambdaUpdate().eq(Bucao_user::getRfno, bucao_user.getRfno()).eq(Bucao_user::getRfid, bucao_user.getRfid()).eq(Bucao_user::getUserId, bucao_user.getUserId()));
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }

    }

    /**删除接口
     *
     * @param userId
     * @param rfno
     * @param rfid
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除接口",notes="根据复合主键（用户id，布草种类）删除")
    public Result<?> delete(@RequestParam String userId,
                            @RequestParam String rfno,
                            @RequestParam String rfid)
    {

        try {
            // Bucao_user bucao=Bucao_userMapper.selectOne(Wrappers.<Bucao_user>lambdaQuery().eq(Bucao_user::getRfno,Bucao_user.getRfno()).eq(Bucao_user::getRfid,Bucao_user.getRfid()));
            QueryWrapper<Bucao_user> wrapper = new QueryWrapper<>();
            wrapper.eq("rfno", rfno).eq("rfid", rfid).eq("user_id", userId);
            if (bucao_userMapper.selectOne(wrapper) == null) {
                return Result.error("-1", "该记录不存在");
            } else {
                bucao_userMapper.updatebucao(rfno, rfid, "已回收"); //归还布草后布草的状态变为已回收
                int rows = bucao_userMapper.delete(wrapper);
                System.out.println(userId + "," + rfid + rfno + "该记录删除成功");
                return Result.success();
            }
            }catch(Exception e){
                System.out.println(e.toString());
                return Result.error("-1", "后台出错啦，请联系开发人员");
            }
    }

    /**分页查询:面向用户的接口
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @param userid
     * @return
     */
    @GetMapping("foruser")
    @ApiOperation(value = "分页查询接口",notes="面向用户接口")
    public Result<?> findPageuser(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestParam(defaultValue = "") String userid)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        try {
            if(pageNum<1){
                return Result.error("-1","pageNum不能小于1");
            }
            if(pageSize<1){
                return Result.error("-1","pageSize不能小于1");
            }
            if(user_infoMapper.selectById(userid)==null)
            {
                return Result.error("-1",userid+"用户不存在");
            }
            QueryWrapper<Bucao_user> wrapper = new QueryWrapper<>();

            if (StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
            {
                wrapper.eq("user_id", userid).like("rfid", search).or().like("rfno", search).or().like("room_id", search).or().like("user_name", search);//eq(a,b)<=>a=b
            } else {
                wrapper.eq("user_id", userid);
            }
            Page<Bucao_user> Bucao_user_page = bucao_userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
            return Result.success(Bucao_user_page);
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错了，请联系开发人员");
        }
    }

    /**
     * 选择布草分配不足4件的病人
     * @return
     */
    @GetMapping("/forbucao")
    @ApiOperation(value = "查询布草分配不足4件的病人接口",notes="查询布草分配不足4件的病人")
    public Result<?> selectforbucao(){
        List<Map<String, Object>> list=bucao_userMapper.selectforbucao();
        return Result.success(list);
    }

    /**分页查询:面向管理员的接口
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询接口",notes="面向管理员的接口")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        //Page<Object> page= new Page<>(pageNum,pageSize);//分页对象

        //LambdaQueryWrapper<RFid_kinds> qw = Wrappers.<User>lambdaQuery().like(User::getName, "张").and(u -> u.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        //LambdaQueryWrapper<Bucao_user> wrapper = Wrappers.<Bucao_user>lambdaQuery();
        if(pageNum<1){
            return Result.error("-1","pageNum不能小于1");
        }
        if(pageSize<1){
            return Result.error("-1","pageSize不能小于1");
        }
        QueryWrapper<Bucao_user> wrapper=new QueryWrapper<>();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {

            wrapper.like("rfid",search).or().like("rfno",search).or().like("user_id",search).or().like("room_id",search).or().like("user_name",search);//eq(a,b)<=>a=b
        }
        Page<Bucao_user> Bucao_user_page=bucao_userMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(Bucao_user_page);
    }
    /**批量删除接口:复合主键
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除接口",notes="根据复合id批量删除：ids=[rfno,rfid,user_id],返回删除成功的数据个数")
    public Result<?> deleteBatch(@RequestBody List<List<String>> ids) {
        Integer success=0;
        try {
            for (List<String> id : ids) {
                bucao_userMapper.updatebucao(id.get(0), id.get(1), "已回收");//对应布草的状态更新为已回收
                QueryWrapper<Bucao_user> wrapper = new QueryWrapper<>();
                wrapper.eq("rfno", id.get(0)).eq("rfid", id.get(1)).eq("user_id", id.get(2));
                if (bucao_userMapper.selectOne(wrapper) != null) {
                    bucao_userMapper.delete(wrapper);
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

        List<Bucao_user> all = bucao_userMapper.selectList(null);
        for (Bucao_user Bucao_user : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("用户账号", Bucao_user.getUserId());
            row1.put("用户姓名", Bucao_user.getUserName());
            row1.put("所在病房", Bucao_user.getRoomId());
            row1.put("布草RFID编号", Bucao_user.getRfno()+Bucao_user.getRfid());

            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("布草-用户信息数据表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

//    /**
//     * Excel导入
//     * 导入的模板可以使用 Excel导出的文件
//     *
//     * @param file Excel
//     * @return
//     * @throws IOException
//     */
//    @PostMapping("/import")
//    @ApiOperation(value = "excel导入接口",notes="excel导入接口")
//    public Result<?> upload(@ApiParam(value = "选择excel文件") @Valid @RequestPart(value = "file") MultipartFile file) throws IOException {
//        Integer num = 0;//统计导入成功的记录条数
//        try {
//            InputStream inputStream = file.getInputStream();
//            List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
//            List<Bucao_user> saveList = new ArrayList<>();
//            for (List<Object> row : lists) {
//                Bucao_user Bucao_user = new Bucao_user();
//                Bucao_user.setRfno(row.get(0).toString());
//                Bucao_user.setRfid(row.get(1).toString());
//
//                saveList.add(Bucao_user);
//            }
//            for (Bucao_user Bucao_user : saveList) {
//                System.out.println(Bucao_user);
//                if (Bucao_user.getRfid() != null && Bucao_user.getRfno() != null) {
//                    bucao_userMapper.insert(Bucao_user);
//                    num=num+1;
//                }
//            }
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }
//        return Result.success(num);
//    }
}
