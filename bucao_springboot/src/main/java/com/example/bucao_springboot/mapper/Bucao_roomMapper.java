package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.bucao_springboot.entity.Bucao_room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface Bucao_roomMapper extends BaseMapper<Bucao_room> {
    /**
     * 查询出没有分配充足的布草的房间
     * @return
     */
    @Select("select * from room_info where id not in (select room_id from bucao_room group by room_id having count(room_id)>=4)")
    List<Map<String, Object>> getrooms();
}
