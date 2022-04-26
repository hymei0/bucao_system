package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

@TableName("Bucao_room")
@Data
public class Bucao_room {
    /**
     *
     */
    @MppMultiId
    private String rfno;

    /**
     *
     */
    @MppMultiId
    private String rfid;

    /**
     *
     */
    @MppMultiId
    private String roomId;
    /**
     *
     */
    @TableField()
    private String roomSection;
    /**
     *
     */
    @TableField()
    private String bucaoSection;

    @TableField(exist = false)
    private String RFIDX;



}
