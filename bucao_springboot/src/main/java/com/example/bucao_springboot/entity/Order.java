package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

@TableName("USER_ROOM")

@Data
public class Order {
    @MppMultiId // 复合主键
    private String userid;
    @MppMultiId // 复合主键
    private String roomId;

    @TableField(exist = false)
    private String uname;
    @TableField(exist = false)
    private String sex;
    @TableField(exist = false)
    private String telephone;
    @TableField(exist = false)
    private String address;

    private int days;

    private double expenses;
}
