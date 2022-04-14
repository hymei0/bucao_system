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
import com.example.bucao_springboot.entity.Bucao_info;
import com.example.bucao_springboot.entity.Order;
import com.example.bucao_springboot.entity.Order;
import com.example.bucao_springboot.mapper.OrderMapper;
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
@RequestMapping("/Order")
public class OrderController {
    @Resource
    OrderMapper OrderMapper;

    //新增接口
    @PostMapping
    public Result<?> save(@RequestBody Order order)
    {
        try {
            Order user=OrderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getUserid,order.getUserid()).or().eq(Order::getRoomId,order.getRoomId()));
            if(user==null) {
                OrderMapper.insert(order);
                System.out.println("Order已添加用户"+order.getUserid()+"的订单信息：");
                return Result.success();
            }
            else
            {
                return Result.error("-1","该用户已存在");
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","系统后台出错啦，请联系工作人员");
        }
    }
    //更新接口
    @PutMapping
    public Result<?> update(@RequestBody Order order)
    {
        try {
            System.out.println(order);
            OrderMapper.update(order.getDays(),order.getExpenses(),order.getRoomId(),order.getUserid());

            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","更新失败");
        }

    }


    //删除接口
    @DeleteMapping
    public Result<?> delete(@RequestParam String userid,
                            @RequestParam String roomId)
    {
        try {
            // Bucao_info bucao=bucao_infoMapper.selectOne(Wrappers.<Bucao_info>lambdaQuery().eq(Bucao_info::getRfno,bucao_info.getRfno()).eq(Bucao_info::getRfid,bucao_info.getRfid()));
            QueryWrapper<Order> wrapper = new QueryWrapper<>();

            wrapper.eq("userid", userid).eq("room_id", roomId);
            int rows=OrderMapper.delete(wrapper);
            //int rows = OrderMapper.delete1(userid,roomid);
            return Result.success();
        }catch (Exception e){
            return Result.error("-1",e.toString());
        }
    }

    //无条件查询
    @GetMapping("/selectall")
    public Result<?>  selectall(){
        return Result.success(OrderMapper.GetOrder());
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {

        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            Page<Order> Order_page=OrderMapper.findPage(new Page<>(pageNum,pageSize),search);
            return Result.success(Order_page);
        }
        else{
            Page<Order> Order_page=OrderMapper.findPage1(new Page<>(pageNum,pageSize));
            return Result.success(Order_page);
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
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("userid", id.get(0)).eq("room_id", id.get(1));
            OrderMapper.delete(wrapper);
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

        List<Order> all = OrderMapper.selectList(null);
        for (Order Order : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("证件号", Order.getUserid());
            row1.put("姓名", Order.getUname());
            row1.put("性别", Order.getSex());
            row1.put("联系电话", Order.getTelephone());
            row1.put("地址", Order.getAddress());
            row1.put("病房号", Order.getRoomId());
            row1.put("住院天数", Order.getDays());
            row1.put("应缴费用", Order.getExpenses());
            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("订单数据表数据表", "UTF-8");
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
        List<Order> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Order Order = new Order();
            Order.setUserid(row.get(0).toString());
            Order.setRoomId(row.get(6).toString());
            Order.setDays(Integer.parseInt(row.get(7).toString()));
            Order.setExpenses(Double.parseDouble(row.get(8).toString()));


            saveList.add(Order);
        }
        for (Order Order : saveList) {

            if(Order.getUserid()!=null&&Order.getRoomId()!=null)
            {
                OrderMapper.insert(Order);
            }
        }
        return Result.success();
    }
}
