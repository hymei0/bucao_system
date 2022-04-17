package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@TableName("User_info")
@Data
public class User_info {
    @TableId(value = "ID", type = IdType.AUTO)
    private String ID;
    private String uname;
    private String portrait;
    private String psd;
    private String telephone;
    private String address;
    private String sex;
    private String roles;

    @TableField(exist = false)
    private List<Bucao_info> bucaoList;

    @TableField(exist = false)
    private String token;

}
