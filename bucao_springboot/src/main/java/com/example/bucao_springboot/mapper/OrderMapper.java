package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.entity.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.address,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid where user_room.userid like concat('%',#{search}, '%')\n"+
             "or user_room.room_id like concat('%',#{search}, '%')")
    Page<Order> findPage(Page<Order>page,String search);

    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.address,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid ")
    Page<Order> findPage1(Page<Order>page);

    @Select("SELECT `user_room`.* ,user_info.uname , user_info.sex ,user_info.address,user_info.telephone from `user_room`\n" +
            " left join user_info  on user_info.id = user_room.userid ")
    List<Order> GetOrder();
    @Delete("delete from user_room where userid=#{userid} and room_id=#{roomid}")
    int delete1(String userid, String roomid);
    @Update("update user_room set days=#{days},expenses=#{exp} where userid=#{id2} and room_id=#{id1}")
     void update(Integer days,Double exp,String id1,String id2);
}
