package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.entity.Bucao_user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper

public interface Bucao_userMapper extends BaseMapper<Bucao_user> {
    @Update("update bucao_info set state='使用中' where rfno=#{rfno} and rfid=#{rfid}")
    void updatebucao(String rfno, String rfid);
}
