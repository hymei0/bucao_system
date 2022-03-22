package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Manager_info")
@Data
public class Manager_info {
    @TableId(value = "ID", type = IdType.AUTO)
    private String ID;
    private String psd;
    private String telephone;
    private String address;
    private String sex;

}
