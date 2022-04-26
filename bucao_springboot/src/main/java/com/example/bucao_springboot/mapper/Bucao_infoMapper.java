package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.common.Result;
import com.example.bucao_springboot.controller.entity.InData;
import com.example.bucao_springboot.controller.entity.Outdata;
import com.example.bucao_springboot.entity.Bucao_info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface Bucao_infoMapper extends BaseMapper<Bucao_info> {
    //按入库月份对布草信息进行统计
    @Select("select DATE_FORMAT(indate, \"%m\") as month, count(1) as num from bucao_info where year(bucao_info.indate)= year( curdate( )) group by DATE_FORMAT(indate, \"%m\");")
    List<InData> Indata();
    //按报废月份对布草信息进行统计
    @Select("select DATE_FORMAT(outdate, \"%m\") as month, count(1) as num from bucao_info  where( outdate is true  and year( bucao_info.outdate ) = year( curdate( )))  group by DATE_FORMAT(outdate, \"%m\");")
    List<Outdata> Outdata();

    @Select("select * from bucao_info where RFNO like #{rfid}\"%\"and state like '闲置%';")
    List<Map<String, Object>> selectbucaoforuser(String rfid);
    @Select("select rfno from rfid_kinds where (RFNO=#{rfno} and RFNO not in (select rfno from bucao_info where rfno=#{rfno} and RFID=#{rfid}));")
    String isValid(String rfno,String rfid);
}
