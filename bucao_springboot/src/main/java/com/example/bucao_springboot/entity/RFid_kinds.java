package com.example.bucao_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("rfid_kinds")

@Data
@ApiModel(value = "RFID标签分类信息", description = "江南大学附属医院RFID标签分类信息")
public class RFid_kinds {

    @ApiModelProperty(value = "分类编号")
    @TableId(value = "RFNO", type = IdType.AUTO)
    private String RFNO;
    @ApiModelProperty(value = "种类名称")
    private String kind;
    @ApiModelProperty(value = "库存")
    private int stock;
    @ApiModelProperty(value = "所属部门")
    private String section;
    @ApiModelProperty(value = "二级分类名称")
    private String note;
}
