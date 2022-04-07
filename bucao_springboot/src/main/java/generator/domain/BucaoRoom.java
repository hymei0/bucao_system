package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName bucao_room
 */
@TableName(value ="bucao_room")
@Data
public class BucaoRoom implements Serializable {
    /**
     * 
     */
    @TableId
    private String rfno;

    /**
     * 
     */
    @TableId
    private String rfid;

    /**
     * 
     */
    @TableId
    private String roomId;

    /**
     * 
     */
    private Integer num;

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
        BucaoRoom other = (BucaoRoom) that;
        return (this.getRfno() == null ? other.getRfno() == null : this.getRfno().equals(other.getRfno()))
            && (this.getRfid() == null ? other.getRfid() == null : this.getRfid().equals(other.getRfid()))
            && (this.getRoomId() == null ? other.getRoomId() == null : this.getRoomId().equals(other.getRoomId()))
            && (this.getNum() == null ? other.getNum() == null : this.getNum().equals(other.getNum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRfno() == null) ? 0 : getRfno().hashCode());
        result = prime * result + ((getRfid() == null) ? 0 : getRfid().hashCode());
        result = prime * result + ((getRoomId() == null) ? 0 : getRoomId().hashCode());
        result = prime * result + ((getNum() == null) ? 0 : getNum().hashCode());
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
        sb.append(", roomId=").append(roomId);
        sb.append(", num=").append(num);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}