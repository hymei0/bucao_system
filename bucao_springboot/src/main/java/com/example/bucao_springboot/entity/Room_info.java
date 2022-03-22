package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Room_info")
@Data
public class Room_info {
    @TableId(value = "room_id", type = IdType.AUTO)
    private String room_id;
    private String section;
}
