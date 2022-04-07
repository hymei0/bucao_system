package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

@TableName("Bucao_user")
@Data
public class Bucao_user {
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
    @MppMultiId
    private String userId;

    /**
     *
     */
    private String userName;

    @TableField(exist = false)
    private String RFIDX;

}
