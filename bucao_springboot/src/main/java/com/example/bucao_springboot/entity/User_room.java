package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@TableName("USER_ROOM")

@Data
@ApiModel(value = "住院信息", description = "江南大学附属医院病人住院信息")
public class User_room {

    @MppMultiId // 复合主键
    @ApiModelProperty(value = "用户ID")
    private String userid;
    @MppMultiId // 复合主键
    @ApiModelProperty(value = "病房ID")
    private String roomid;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户姓名")
    private String uname;
    @TableField(exist = false)
    @ApiModelProperty(value = "用户性别")
    private String sex;
    @TableField(exist = false)
    @ApiModelProperty(value = "用户电话")
    private String telephone;
    /**
     * 出院的病人的住院记录依然存在，所以得加上住院时日期作为复合主键
     */
    @MppMultiId // 复合主键
    @ApiModelProperty(value = "入院时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date comeTime;

    @ApiModelProperty(value = "出院时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date outTime;

    private double expenses;
}
