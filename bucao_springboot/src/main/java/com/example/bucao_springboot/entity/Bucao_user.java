package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Bucao_user")
@Data
public class Bucao_user {
    @TableId(value = "RFID", type = IdType.AUTO)
    private String RFID;
    private String user_id;
}
