package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 *
 * @TableName manager_info
 */
@TableName(value ="manager_info")
@Data
@ApiModel(value = "管理员信息", description = "江南大学附属医院布草智能柜管理员信息")
public class ManagerInfo implements Serializable {
    /**
     * 管理员id
     */
    @TableId
    @ApiModelProperty(value = "管理员ID")
    private String id;

    /**
     * 管理员姓名
     */
    @ApiModelProperty(value = "管理员姓名")
    private String mname;

    /**
     * 管理员密码
     */
    @ApiModelProperty(value = "管理员密码")
    private String psd;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private String pri;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String portrait;

    /**
     * 电话
     */
    @ApiModelProperty(value = "联系电话")
    private String telephone;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Email(regexp = "输入正确的邮箱格式")
    private String email;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
