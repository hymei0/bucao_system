package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

import java.sql.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date indate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date outdate;
}
