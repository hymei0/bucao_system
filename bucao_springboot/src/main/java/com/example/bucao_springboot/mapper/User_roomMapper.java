package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.controller.entity.rooms;
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

    @Select("SELECT * FROM USER_ROOM WHERE userid=#{userId} AND roomid=#{roomId}")
    User_room selectById(String userId, String roomId);
    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid where( user_room.userid like concat('%',#{search}, '%')\n"+
            "or user_room.roomid like concat('%',#{search}, '%'))and user_room.userid=#{userid} ")
    Page<User_room> findPageuser(Page<Object> objectPage, String search, String userid);

    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid where user_room.userid=#{userid} ")
    Page<User_room> findPage1user(Page<Object> objectPage, String userid);
    @Select("select room_info.id from room_info where room_info.id not in(select roomid from  user_room group by roomid having count(roomid)>=4);")
    List<String> Getsuitableroom();

    /**
     * 选择未办理住院的病人或者曾经住过院现在已经出院的病人
     * @return
     */
    @Select("select user_info.id from user_info where id not in(select userid from user_room where out_time is not true)")
    List<String> Getsuitableuser();
}
