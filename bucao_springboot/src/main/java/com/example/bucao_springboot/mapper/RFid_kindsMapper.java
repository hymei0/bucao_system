package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.controller.entity.Kinds_stocks;
import com.example.bucao_springboot.controller.entity.kinds_Section;
import com.example.bucao_springboot.entity.RFid_kinds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RFid_kindsMapper extends BaseMapper<RFid_kinds> {
    @Select("select sum(stock) as stocks ,kind from RFID_kinds group by kind;")
    List<Kinds_stocks> STOCKS();
    @Select("select sum(stock) as stocks ,section from RFID_kinds group by section;")
    List<kinds_Section> STOCKS_setion();

    /**
     * 查找到没有关联其他表的病房编号
     * @return
     */
    @Select("select RFNO from RFID_KINDS where RFNO not in \n" +
            "(select distinct RFNO from bucao_info)\n" +
            "and RFNO=#{id};")
    List<String> selectUniqueRFID(String id);
}
