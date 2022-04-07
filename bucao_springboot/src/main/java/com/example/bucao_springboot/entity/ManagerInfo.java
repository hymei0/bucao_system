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
    private String id;

    /**
     *
     */
    private String mname;

    /**
     *
     */
    private String psd;

    /**
     *
     */
    private String pri;

    /**
     *
     */
    private String portrait;

    /**
     *
     */
    private String telephone;

    /**
     *
     */
    private String address;

    /**
     *
     */
    private String email;

    /**
     *
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
        ManagerInfo  other = (ManagerInfo) that;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMname() == null) ? 0 : getMname().hashCode());
        result = prime * result + ((getPsd() == null) ? 0 : getPsd().hashCode());
        result = prime * result + ((getPri() == null) ? 0 : getPri().hashCode());
        result = prime * result + ((getPortrait() == null) ? 0 : getPortrait().hashCode());
        result = prime * result + ((getTelephone() == null) ? 0 : getTelephone().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
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
        sb.append(", mname=").append(mname);
        sb.append(", psd=").append(psd);
        sb.append(", pri=").append(pri);
        sb.append(", portrait=").append(portrait);
        sb.append(", telephone=").append(telephone);
        sb.append(", address=").append(address);
        sb.append(", email=").append(email);
        sb.append(", sex=").append(sex);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
