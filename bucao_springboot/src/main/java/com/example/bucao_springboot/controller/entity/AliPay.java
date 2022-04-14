package com.example.bucao_springboot.controller.entity;

import lombok.Data;

@Data
public class AliPay {

    private String subject;       //商品名称
    private String traceNo;       //订单编号
    private String totalAmount;    //总金额

}
