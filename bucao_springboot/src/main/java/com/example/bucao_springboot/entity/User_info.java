package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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

}
