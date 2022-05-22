package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.entity.Room_info;
import com.example.bucao_springboot.entity.User_info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface Room_infoMapper extends BaseMapper<Room_info> {
    /**
     * 在bucao_user表中添加记录时过滤掉已经有病人住满的病房：病人办理出院的记录仍然存在，所以要过滤掉已经出院的情况
     * @return
     */
    @Select("select * from room_info where id not in (select roomid from user_room where out_time IS NOT TRUE  group by roomid having count(roomid)>=4);")
    List<Map<String, Object>> selectforbucao();

    /**
     * 查找到没有关联其他表的病房编号
     * @return
     */
    @Select("select id from room_info where id not in \n" +
            "(select distinct roomid from user_room)\n" +
            "and id not in (select distinct room_id from bucao_room) and id=#{id};")
    List<String> selectUniqueRoom(String id);
}
