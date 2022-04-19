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
    /**
     * 分页查询search不为空
     * @param page
     * @param search
     * @return
     */
    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid where user_room.userid like concat('%',#{search}, '%')\n"+
            "or user_room.roomid like concat('%',#{search}, '%')")
    Page<User_room> findPage(Page<User_room>page, String search);

    /**
     * 分页查询search为空
     * @param page
     * @return
     */
    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid ")
    Page<User_room> findPage1(Page<User_room>page);

    /**
     *
     * @return
     */

    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid ")
    /**
     * User_room表根据复合主键删除记录
     */
    List<User_room> GetUser_room();
    @Delete("delete from user_room where userid=#{userid} and roomid=#{roomid}")
    int delete1(String userid, String roomid);

    /**
     * User_room表根据复合主键更新记录
     * @param date1
     * @param date2
     * @param exp
     * @param id1
     * @param id2
     */
    @Update("update user_room set come_time=#{date1},out_time=#{date2},expenses=#{exp} where userid=#{id2} and roomid=#{id1}")
    void update(Date date1, Date date2,Double exp, String id1, String id2);

    /**
     * 复合主键根据id查询
     * @param userId
     * @param roomId
     * @return
     */
    @Select("SELECT * FROM USER_ROOM WHERE userid=#{userId} AND roomid=#{roomId}")
    User_room selectById(String userId, String roomId);

    /**
     *面向用户的分页查询，search不为空
     * @param objectPage
     * @param search
     * @param userid
     * @return
     */
    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid where( user_room.userid like concat('%',#{search}, '%')\n"+
            "or user_room.roomid like concat('%',#{search}, '%'))and user_room.userid=#{userid} ")
    Page<User_room> findPageuser(Page<Object> objectPage, String search, String userid);

    /**
     * 面向用户的分页查询，search为空
     * @param objectPage
     * @param userid
     * @return
     */
    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid where user_room.userid=#{userid} ")
    Page<User_room> findPage1user(Page<Object> objectPage, String userid);

    /**
     * 选择没有住满四个人的病房
     * @return
     */
    @Select("select room_info.id from room_info where room_info.id not in(select roomid from  user_room group by roomid having count(roomid)>=4);")
    List<String> Getsuitableroom();

    /**
     * 选择未办理住院的病人或者曾经住过院现在已经出院的病人
     * @return
     */
    @Select("select user_info.id from user_info where id not in(select userid from user_room where out_time is not true)")
    List<String> Getsuitableuser();

    /**
     * 更行删除记录时寻妖更新布草的状态
     * @param roomid
     * @param state
     */
    @Update("update bucao_info set state=#{state} where rfno in (select rfno from bucao_room where room_id=#{roomid} ) and rfid in (select rfid from bucao_room where room_id=#{roomid} );")
    void updatebucao(String roomid, String state);
}
