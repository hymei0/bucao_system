package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.entity.User_room;
import com.example.bucao_springboot.entity.User_room;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Date;
import java.util.List;

@Mapper
public interface User_roomMapper extends BaseMapper<User_room> {
    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid where user_room.userid like concat('%',#{search}, '%')\n"+
            "or user_room.roomid like concat('%',#{search}, '%')")
    Page<User_room> findPage(Page<User_room>page, String search);

    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid ")
    Page<User_room> findPage1(Page<User_room>page);

    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid ")
    List<User_room> GetUser_room();
    @Delete("delete from user_room where userid=#{userid} and roomid=#{roomid}")
    int delete1(String userid, String roomid);
    @Update("update user_room set come_time=#{date1},out_time=#{date2},expenses=#{exp} where userid=#{id2} and roomid=#{id1}")
    void update(Date date1, Date date2,Double exp, String id1, String id2);

    @Select("SELECT * FROM USER_ROOM WHERE userid=#{userId}AND roomid=#{roomId}")
    User_room selectById(String userId, String roomId);
}
