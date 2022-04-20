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
import com.example.bucao_springboot.entity.Bucao_room;
import com.example.bucao_springboot.entity.Bucao_user;
import com.example.bucao_springboot.entity.Room_info;
import com.example.bucao_springboot.entity.User_room;
import com.example.bucao_springboot.mapper.Bucao_userMapper;
import com.example.bucao_springboot.mapper.User_roomMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/User_room")
public class User_roomController {
    @Resource
    User_roomMapper User_roomMapper;

    /**新增接口
     *
     * @param user_room
     * @return
     */
    @PostMapping
    public Result<?> save(@RequestBody User_room user_room)
    {
        try {
            User_room user=User_roomMapper.selectOne(Wrappers.<User_room>lambdaQuery().eq(User_room::getUserid,user_room.getUserid()).eq(User_room::getRoomid,user_room.getRoomid()).eq(User_room::getComeTime,user_room.getComeTime()));
            if(user==null) {
                User_roomMapper.updatebucao(user_room.getRoomid(),"使用中");
                User_roomMapper.insert(user_room);
                System.out.println("User_room已添加用户"+user_room.getUserid()+"的住院信息：");
                return Result.success();
            }
            else
            {
                return Result.error("-1","该部门已存在");
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","系统后台出错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param User_room
     * @return
     */
    @PutMapping
    public Result<?> update(@RequestBody User_room User_room)
    {
        try {
            User_roomMapper.update(User_room.getComeTime(),User_room.getOutTime(),User_room.getExpenses(),User_room.getRoomid(),User_room.getUserid());
            System.out.println(User_room+"信息更新成功");
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","更新失败");
        }

    }

    /**
     * 病人和病房时多对多的关系，一个病人住一个病房，一个病人可能多次住院，每次病房不一定一样
     * 所以可以根据病人id查询出病人所在病房
     * @param userid
     * @return
     */
    @GetMapping("/getroomid")
    public Result<?> selecte(@RequestParam String userid)
    {
        try {
            // Bucao_user bucao=Bucao_userMapper.selectOne(Wrappers.<Bucao_user>lambdaQuery().eq(Bucao_user::getRfno,Bucao_user.getRfno()).eq(Bucao_user::getRfid,Bucao_user.getRfid()));
            QueryWrapper<User_room> wrapper = new QueryWrapper<>();

            wrapper.eq("userid", userid);

            List<User_room> ur=User_roomMapper.selectList(wrapper);
            if(ur.size()>0)
            {
                return Result.success(ur.get(0).getRoomid());
            }
            else{
                return Result.error("-1","登录信息失效");
            }

        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","后台出错啦，请联系开发人员");
        }
    }

    /**删除接口
     *
     * @param userid
     * @param roomid
     * @param comeTime
     * @return
     */
    @DeleteMapping
    public Result<?> delete(@RequestParam String userid,
                            @RequestParam String roomid,
                            @RequestParam Date comeTime)
    {
        try {
            // Bucao_info bucao=bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,bucao_info.getRfno()).eq(Bucao_info::getRfid,bucao_info.getRfid()));
            QueryWrapper<User_room> wrapper = new QueryWrapper<>();

            wrapper.eq("userid", userid).eq("roomid", roomid).eq("come_time", comeTime);
            int rows=User_roomMapper.delete(wrapper);
            //int rows = User_roomMapper.delete1(userid,roomid);
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","请先删除订单信息中相关记录");
        }
    }

    /**查询所有记录
     *
     * @return
     */
    @GetMapping("/selectall")
    public Result<?>  selectall(){
        return Result.success(User_roomMapper.GetUser_room());
    }

    /**
     * 为新增订单挑选出住院且需要缴费得病人
     * @return
     */
    @GetMapping("/fororder")
    public  Result<?>ForOrder()
    {
        QueryWrapper<User_room> wrapper = new QueryWrapper<>();
        wrapper.gt("expenses",0.00);
        List<User_room> list= User_roomMapper.selectList(wrapper);
        return Result.success(list);
    }

    /**
     * 根据userid查询出需要缴费得住院记录:与order表相关
     * @param userid
     * @return
     */
    @GetMapping("/getRooms")
    public Result<?> GetRooms(@RequestParam String userid){
        QueryWrapper<User_room> queryWrapper = new QueryWrapper<>();
        List<User_room> list=User_roomMapper.selectList(queryWrapper.eq("userid",userid).gt("expenses",0.00));
        return Result.success(list);
    }


    /**查询未住满的病房号
     *
     * @return
     */
    @GetMapping("/suitableroom")
    public Result<?>  suitableroom(){
        return Result.success(User_roomMapper.Getsuitableroom());
    }

    /**查询未办理住院的病人
     *
     * @return
     */
    @GetMapping("/suitableuser")
    public Result<?>  suitableuser(){
        return Result.success(User_roomMapper.Getsuitableuser());
    }

    /**分页查询:面向管理员的接口
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

        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            Page<User_room> User_room_page=User_roomMapper.findPage(new Page<>(pageNum,pageSize),search);
            return Result.success(User_room_page);
        }
        else{
            Page<User_room> User_room_page=User_roomMapper.findPage1(new Page<>(pageNum,pageSize));
            return Result.success(User_room_page);
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
    public Result<?> findPageuser(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                                  @RequestParam(defaultValue = "") String userid)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {

        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            Page<User_room> User_room_page=User_roomMapper.findPageuser(new Page<>(pageNum,pageSize),search,userid);
            return Result.success(User_room_page);
        }
        else{
            Page<User_room> User_room_page=User_roomMapper.findPage1user(new Page<>(pageNum,pageSize),userid);
            return Result.success(User_room_page);
        }
    }


    /**批量删除接口:复合主键
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<List<String>> ids) {

        for(List<String> id:ids)
        {
            QueryWrapper<User_room> wrapper = new QueryWrapper<>();
            wrapper.eq("userid", id.get(0)).eq("roomid", id.get(1)).eq("come_time", id.get(2));
            User_roomMapper.delete(wrapper);
        }
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

        List<User_room> all = User_roomMapper.selectList(null);
        for (User_room User_room : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("ID", User_room.getUserid());
            row1.put("姓名", User_room.getUname());
            row1.put("性别", User_room.getSex());
            row1.put("联系电话", User_room.getTelephone());
            row1.put("病房号", User_room.getRoomid());
            row1.put("入院时间", User_room.getComeTime());
            row1.put("出院时间", User_room.getOutTime());
            row1.put("应缴费用", User_room.getExpenses());
            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("住院信息数据表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

}
