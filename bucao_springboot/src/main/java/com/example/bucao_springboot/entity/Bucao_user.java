package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("Bucao_user")
@Data
@ApiModel(value = "布草-用户信息", description = "江南大学附属医院病人持有病服信息")
public class Bucao_user {
    /**
     *
     */
    @MppMultiId
    @ApiModelProperty(value = "布草类别")
    private String rfno;

    /**
     *
     */
    @MppMultiId
    @ApiModelProperty(value = "布草编号")
    private String rfid;

    /**
     *
     */
    @MppMultiId
    @ApiModelProperty(value = "病房编号")
    private String roomId;
    /**
     *
     */
    @MppMultiId
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     *
     */
    @ApiModelProperty(value = "用户名字")
    private String userName;

    @TableField(exist = false)
    @ApiModelProperty(value = "RFID编号")
    private String RFIDX;

}
