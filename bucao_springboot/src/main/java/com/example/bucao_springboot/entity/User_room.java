package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

import java.sql.Date;

@TableName("USER_ROOM")

@Data
public class User_room {

    @MppMultiId // 复合主键
    private String userid;
    @MppMultiId // 复合主键
    private String roomid;

    @TableField(exist = false)
    private String uname;
    @TableField(exist = false)
    private String sex;
    @TableField(exist = false)
    private String telephone;
    /**
     * 出院的病人的住院记录依然存在，所以得加上住院时日期作为复合主键
     */
    @MppMultiId // 复合主键
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date comeTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date outTime;

    private double expenses;
}
