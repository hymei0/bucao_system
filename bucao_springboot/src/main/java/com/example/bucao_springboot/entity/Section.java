package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("section")
@Data
public class Section {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String na;
}
