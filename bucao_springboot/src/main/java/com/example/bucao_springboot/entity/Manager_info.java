package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("Manager_info")
@Data
public class Manager_info {
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Manager_info  other = (Manager_info) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getMname() == null ? other.getMname() == null : this.getMname().equals(other.getMname()))
                && (this.getPsd() == null ? other.getPsd() == null : this.getPsd().equals(other.getPsd()))
                && (this.getPri() == null ? other.getPri() == null : this.getPri().equals(other.getPri()))
                && (this.getPortrait() == null ? other.getPortrait() == null : this.getPortrait().equals(other.getPortrait()))
                && (this.getTelephone() == null ? other.getTelephone() == null : this.getTelephone().equals(other.getTelephone()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()));
    }

}
