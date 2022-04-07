package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bucao_springboot.entity.User_info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

@Mapper
public interface User_infoMapper extends BaseMapper<User_info> {
    // 查询用户名
    @Select("select psd from user where uname=#{uname}")
    User_info selectByName(String username);

    @Update("update user set psd = #{newPass} where id = #{userId}")
    int updatePass(Map<String, Object> map);
}
