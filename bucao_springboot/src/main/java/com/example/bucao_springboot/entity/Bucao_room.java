package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("Bucao_room")
@Data
@ApiModel(value = "布草-病房信息", description = "江南大学附属医院布草病房分布信息")
public class Bucao_room {
    /**
     *
     */
    @ApiModelProperty(value = "布草类别")
    @MppMultiId
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
    @ApiModelProperty(value = "病房ID")
    private String roomId;
    /**
     *
     */
    @TableField()
    @ApiModelProperty(value = "病房所属部门")
    private String roomSection;
    /**
     *
     */
    @TableField()
    @ApiModelProperty(value = "布草所属部门")
    private String bucaoSection;

    @TableField(exist = false)
    @ApiModelProperty(value = "RFID编号")
    private String RFIDX;



}
