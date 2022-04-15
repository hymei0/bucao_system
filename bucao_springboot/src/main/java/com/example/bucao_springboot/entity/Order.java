package com.example.bucao_springboot.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

@TableName("ORDER_INFO")

@Data
public class Order {
    @TableId
    private String orderno;

    private String userId;

    private String roomId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date comeTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date outTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paytime;

    private double expenses;

    private String state;
}
