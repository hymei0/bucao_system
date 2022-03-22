package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("bucao_info")
@Data
public class Bucao_info {
    @TableId(value = "rfno,rfid", type = IdType.AUTO)
    private String rfno;
    private String rfid;
    private String state;
    private int wash_times;
}
