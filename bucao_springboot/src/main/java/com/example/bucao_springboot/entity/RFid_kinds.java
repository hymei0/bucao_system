package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("rfid_kinds")

@Data
public class RFid_kinds {
    @TableId(value = "RFNO", type = IdType.AUTO)
    private String RFNO;
    private String kind;
    private int stock;
}
