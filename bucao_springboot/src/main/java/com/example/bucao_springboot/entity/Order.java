package com.example.bucao_springboot.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("ORDER_INFO")

@Data
@ApiModel(value = "订单信息", description = "江南大学附属医院住院订单信息")
public class Order {
    @ApiModelProperty(value = "订单编号")
    @TableId
    private String orderno;

    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "病房ID")
    private String roomId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "订单创建时间")
    private Date createtime;

    @ApiModelProperty(value = "订单名称")
    private String subject;

    @ApiModelProperty(value = "付费时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paytime;

    @ApiModelProperty(value = "金额")
    private double expenses;
    @ApiModelProperty(value = "订单状态")
    private String state;
}
