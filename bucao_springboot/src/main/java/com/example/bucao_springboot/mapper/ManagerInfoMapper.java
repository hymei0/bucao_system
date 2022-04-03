package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.entity.ManagerInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
* @author we
* @description 针对表【manager_info】的数据库操作Mapper
* @createDate 2022-04-01 22:34:15
* @Entity generator.domain.ManagerInfo
*/
@Mapper
public interface ManagerInfoMapper extends BaseMapper<ManagerInfo> {

    void addManagerInfo(List<ManagerInfo> list);

    List<ManagerInfo> queryManagerInfo();
}




