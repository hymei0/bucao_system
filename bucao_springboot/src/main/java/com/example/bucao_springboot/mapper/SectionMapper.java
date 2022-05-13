package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.entity.Section;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SectionMapper extends BaseMapper<Section> {

    @Select("SELECT * FROM SECTION WHERE NA=#{na}")
    List<Section> selectByNa(String na);

    //查找到没有关联到其他表的部门编号，因为部门名称具有唯一性
    @Select("select id  from section where na not in\n" +
            "(select DISTINCT section from RFID_KINDS)\n" +
            "and na not in(select DISTINCT section from ROOM_info) and id=#{id1}")
    List<String> selectUniqueSection(String id1);
}
