package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Bucao_room")
@Data
public class Bucao_room {
    @TableId(value = "RFNO", type = IdType.AUTO)
    private String RFNO;
    private String RFID;
    private String Room_id;
    private int num;
}
