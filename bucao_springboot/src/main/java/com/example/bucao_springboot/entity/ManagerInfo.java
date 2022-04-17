package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName manager_info
 */
@TableName(value ="manager_info")
@Data
public class ManagerInfo implements Serializable {
    /**
     * 管理员id
     */
    @TableId
    private String id;

    /**
     * 管理员姓名
     */
    private String mname;

    /**
     * 管理员密码
     */
    private String psd;

    /**
     * 权限
     */
    private String pri;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String sex;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
