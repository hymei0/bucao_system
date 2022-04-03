package com.example.bucao_springboot.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
     *
     */
    @TableId
    @Excel(name = "账号", orderNum = "0")
    private String id;

    /**
     *
     */
    @Excel(name = "密码", orderNum = "1")
    private String psd;

    /**
     *
     */
    @Excel(name = "头像", orderNum = "2")
    private String portrait;

    /**
     *
     */
    @Excel(name = "电话", orderNum = "3")
    private String telephone;

    /**
     *
     */
    @Excel(name = "地址", orderNum = "4")
    private String address;

    /**
     *
     */
    @Excel(name = "性别", orderNum = "5")
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
        ManagerInfo other = (ManagerInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPsd() == null ? other.getPsd() == null : this.getPsd().equals(other.getPsd()))
            && (this.getPortrait() == null ? other.getPortrait() == null : this.getPortrait().equals(other.getPortrait()))
            && (this.getTelephone() == null ? other.getTelephone() == null : this.getTelephone().equals(other.getTelephone()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPsd() == null) ? 0 : getPsd().hashCode());
        result = prime * result + ((getPortrait() == null) ? 0 : getPortrait().hashCode());
        result = prime * result + ((getTelephone() == null) ? 0 : getTelephone().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", psd=").append(psd);
        sb.append(", portrait=").append(portrait);
        sb.append(", telephone=").append(telephone);
        sb.append(", address=").append(address);
        sb.append(", sex=").append(sex);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
