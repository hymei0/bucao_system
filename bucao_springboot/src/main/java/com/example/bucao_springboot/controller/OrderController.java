package com.example.bucao_springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.entity.Order;
import com.example.bucao_springboot.entity.RFid_kinds;
import com.example.bucao_springboot.entity.User_room;
import com.example.bucao_springboot.mapper.OrderMapper;
import com.example.bucao_springboot.mapper.Room_infoMapper;
import com.example.bucao_springboot.mapper.User_roomMapper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Order")
@Api(tags = "订单信息管理")
public class OrderController {
    @Resource
    OrderMapper OrderMapper;

    @Resource
    User_roomMapper User_roomMapper;

    /**新增接口
     *
     * @param order
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增订单接口",notes="新增")
    public Result<?> save(@RequestBody Order order)  {

//        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date=sdf.format(System.currentTimeMillis()).toString();
//        order.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
//        System.out.println(order);


        try {
            Order order1=OrderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderno,order.getOrderno()));
            if(order1==null) {
//                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String date=sdf.format(System.currentTimeMillis()).toString();
//                order.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));

                OrderMapper.insert(order);
                System.out.println("Order已添加用户"+order.getUserId()+"的订单信息：");
                System.out.println(order);
                return Result.success();
            }
            else
            {
                return Result.error("-1","该用户已存在");
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
            return Result.error("-1","系统后台出错啦，请联系开发人员");
        }
    }

    /**更新接口
     *
     * @param order
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新接口",notes="更新订单接口")
    public Result<?> update(@RequestBody Order order)
    {
        try {
            OrderMapper.updateById(order);
            return Result.success();
        }catch (Exception e){
            System.out.println(e.toString());
            return Result.error("-1","更新失败");
        }

    }

    /**删除接口
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除接口",notes="根据订单编号删除订单信息")
    public Result<?> delete(@PathVariable String id)
    {
        try {
            int rows=OrderMapper.deleteById(id);
            //int rows = OrderMapper.delete1(userid,roomid);
            return Result.success();
        }catch (Exception e){
            return Result.error("-1",e.toString());
        }
    }

    /**无条件查询
     *
     * @return
     */
    @GetMapping("/selectall")
    @ApiOperation(value = "查询所有订单",notes="无条件查询")
    public Result<?>  selectall(){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=OrderMapper.selectMaps(queryWrapper);
        return Result.success(list);
    }

    /**
     * 支付接口 返回支付连接
     * @param order
     * @return
     */
    @PostMapping("/buy")
    @ApiOperation(value = "支付接口",notes="返回支付链接")
    public Result<?> buy(@RequestBody Order order) {

        String payUrl = "http://localhost:9090/alipay/pay?subject=" + order.getSubject() + "&traceNo=" + order.getOrderno() + "&totalAmount=" + order.getExpenses();
        return Result.success(payUrl);
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
    @ApiOperation(value = "分页查询接口",notes="面向用户")
    public Result<?> findPageuser(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestParam String userid)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery();

        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(Order::getOrderno,search).eq(Order::getUserId, userid);
        }
        else{
            wrapper.eq(Order::getUserId, userid);
        }
        Page<Order> orderpage=OrderMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(orderpage);
    }

    /**分页查询:面向管理员的接口
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询接口",notes="面向管理员")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    //参数：pageNum：当前页，pageSize:每页多少条 search:查询关键字
    {
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery();
        if(StrUtil.isNotBlank(search))//不为null,则进行模糊匹配
        {
            wrapper.like(Order::getOrderno,search);//eq(a,b)<=>a=b
        }
        Page<Order> orderpage=OrderMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(orderpage);
    }

    /**批量删除接口
     *
     * @param ids
     * @return
     */

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量插入接口",notes="根据Id集合批量删除")
    public Result<?> deleteBatch(@RequestBody List<String> ids) {

        OrderMapper.deleteBatchIds(ids);
        return Result.success();
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

        List<Order> all = OrderMapper.selectList(null);
        for (Order Order : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("订单号", Order.getOrderno());
            row1.put("订单名", Order.getSubject());
            row1.put("用户编号", Order.getUserId());
            row1.put("病房号", Order.getRoomId());
            row1.put("创建时间",DateUtil.format(Order.getCreatetime(),"yyyy-MM-dd HH:mm:ss"));
            row1.put("应缴费用", Order.getExpenses());
            row1.put("缴费时间", DateUtil.format(Order.getPaytime(),"yyyy-MM-dd HH:mm:ss"));
            row1.put("支付状态", Order.getState());
            list.add(row1);
        }
        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("订单数据表", "UTF-8");
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
        try {
            InputStream inputStream = file.getInputStream();
            List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
            List<Order> saveList = new ArrayList<>();
            for (List<Object> row : lists) {
                Order Order = new Order();
                Order.setOrderno(row.get(0).toString());
                Order.setUserId(row.get(1).toString());
                Order.setRoomId(row.get(2).toString());
                Order.setRoomId(row.get(3).toString());
                Order.setCreatetime(Date.valueOf(DateUtil.format(DateUtil.parse(row.get(4).toString()), "yyyy-MM-dd")));
                Order.setExpenses(Double.parseDouble(row.get(5).toString()));

                if (row.get(7).toString() == "未支付" ) {
                    Order.setPaytime(null);
                } else {
                    Order.setPaytime(Date.valueOf(DateUtil.format(DateUtil.parse(row.get(6).toString()), "yyyy-MM-dd HH:mm:ss")));
                }
                saveList.add(Order);
            }
            for (Order Order : saveList) {

                if (Order.getOrderno() != null) {
                    OrderMapper.insert(Order);
                    num=num+1;
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return Result.success(num);
    }
}
