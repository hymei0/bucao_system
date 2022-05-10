package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@TableName("User_info")
@Data
@ApiModel(value = "用户信息", description = "江南大学附属医院病人信息")
public class User_info {
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private String ID;
    @ApiModelProperty(value = "用户名")
    private String uname;
    @ApiModelProperty(value = "头像")
    private String portrait;
    @ApiModelProperty(value = "密码")
    private String psd;
    @ApiModelProperty(value = "电话")
    private String telephone;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "角色")
    private String roles;

    @TableField(exist = false)
    @ApiModelProperty(value = "布草列表")
    private List<Bucao_info> bucaoList;
    @ApiModelProperty(value = "token")
    @TableField(exist = false)
    private String token;

}
