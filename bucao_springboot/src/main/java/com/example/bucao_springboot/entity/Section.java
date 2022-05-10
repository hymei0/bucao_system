package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("section")
@Data
@ApiModel(value = "部门信息", description = "江南大学附属医院部门信息")
public class Section {
    @ApiModelProperty(value = "部门ID")
    @TableId(value = "id")
    private String id;
    @ApiModelProperty(value = "部门名称")
    private String na;
}
