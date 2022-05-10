package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@TableName("bucao_info")
@Data
@ApiModel(value = "布草信息", description = "江南大学附属医院布草信息")
public class Bucao_info {

    @MppMultiId // 复合主键
    @ApiModelProperty(value = "布草类别")
    @TableField("rfno")
    private String rfno;

    @MppMultiId // 复合主键
    @ApiModelProperty(value = "布草编号")
    @TableField("rfid")
    private String rfid;

    @ApiModelProperty(value = "布草状态")
    private String state;
    @ApiModelProperty(value = "洗涤次数")
    private Integer washtimes;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "入库时间")
    private Date indate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "出库时间")
    private Date outdate;
}
