package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.entity.Room_info;
import com.example.bucao_springboot.entity.User_info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

@Mapper
public interface Room_infoMapper extends BaseMapper<Room_info> {

}
