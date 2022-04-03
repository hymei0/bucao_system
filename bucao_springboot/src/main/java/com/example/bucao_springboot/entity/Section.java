package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("section")
@Data
public class Section {
    @TableId
    private String id;
    private String na;

}
