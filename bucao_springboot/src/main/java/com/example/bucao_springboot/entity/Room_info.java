package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("Room_info")
@Data
@ApiModel(value = "病房信息", description = "江南大学附属医院病房信息")
public class Room_info {
    @ApiModelProperty(value = "病房ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    @ApiModelProperty(value = "所属部门")
    private String section;
}
