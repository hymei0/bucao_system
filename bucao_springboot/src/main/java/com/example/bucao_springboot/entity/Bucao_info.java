package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

@TableName("bucao_info")
@Data
public class Bucao_info {

    @MppMultiId // 复合主键
    @TableField("rfno")
    private String rfno;

    @MppMultiId // 复合主键
    @TableField("rfid")
    private String rfid;
    private String state;
    private Integer washtimes;
}
