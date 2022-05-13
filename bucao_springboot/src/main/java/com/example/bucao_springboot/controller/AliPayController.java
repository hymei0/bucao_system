package com.example.bucao_springboot.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bucao_springboot.controller.entity.AliPay;
import com.example.bucao_springboot.entity.Order;
import com.example.bucao_springboot.mapper.OrderMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
// http://6dmtdf.natappfree.cc/alipay/notify
@RestController
@RequestMapping("/alipay")
@Api(tags = "支付宝支付接口")
public class AliPayController {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 支付接口
     * @param aliPay
     * @return
     */
    @GetMapping("/pay")
    @ApiOperation(value = "支付接口",notes="支付接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " aliPay.traceNo",value = "订单号",required = true),
            @ApiImplicitParam(name = "aliPay.subject",value = "交易名称",required = true),
            @ApiImplicitParam(name = " aliPay.totalAmount",value = "总金额",required = true)
    })
    public String pay(AliPay aliPay) {
        AlipayTradePagePayResponse response;
        try {
            //  发起API调用（以创建当面付收款二维码为例）
            response = Factory.Payment.Page()
                    .pay(aliPay.getSubject(), aliPay.getTraceNo(), aliPay.getTotalAmount(), "");
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.getBody();
    }

    /**
     * 支付宝异步回调函数
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/notify")  // 注意这里必须是POST接口
    @ApiOperation(value = "支付宝异步回调函数",notes="支付宝异步回调函数")
    public String payNotify(HttpServletRequest request) throws Exception {
        System.out.println("支付宝回调函数--");
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                System.out.println(name + " = " + request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");

            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单未已支付
                Order order=orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderno,params.get("out_trade_no")));
                order.setState("已支付");

                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date=sdf.format(System.currentTimeMillis()).toString();

                order.setPaytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
                orderMapper.updateById(order);
            }
        }
        return "success";
    }

}
