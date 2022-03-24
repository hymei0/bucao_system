package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

/**
 *
 * @TableName bucao_info
 */
@TableName(value ="bucao_info")
@Data
public class BucaoInfo implements Serializable {
    /**
     *
     */
    @MppMultiId // 复合主键
    @TableField("rfno")
    private String rfno;

    /**
     *
     */
    @MppMultiId // 复合主键
    @TableField("rfid")
    private String rfid;

    /**
     *
     */
    private String state;

    /**
     *
     */
    private Integer washTimes;

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
       BucaoInfo other = (BucaoInfo) that;
        return (this.getRfno() == null ? other.getRfno() == null : this.getRfno().equals(other.getRfno()))
                && (this.getRfid() == null ? other.getRfid() == null : this.getRfid().equals(other.getRfid()))
                && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
                && (this.getWashTimes() == null ? other.getWashTimes() == null : this.getWashTimes().equals(other.getWashTimes()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRfno() == null) ? 0 : getRfno().hashCode());
        result = prime * result + ((getRfid() == null) ? 0 : getRfid().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getWashTimes() == null) ? 0 : getWashTimes().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", rfno=").append(rfno);
        sb.append(", rfid=").append(rfid);
        sb.append(", state=").append(state);
        sb.append(", washTimes=").append(washTimes);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
