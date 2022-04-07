package com.example.bucao_springboot.Service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bucao_springboot.Service.ManagerInfoService;
import com.example.bucao_springboot.entity.ManagerInfo;
import com.example.bucao_springboot.mapper.ManagerInfoMapper;
import generator.domain.RoomInfo;
import generator.mapper.RoomInfoMapper;
import generator.service.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
* @author we
* @description 针对表【manager_info】的数据库操作Service实现
* @createDate 2022-04-01 22:34:15
*/
@Service
public class ManagerInfoServiceImpl extends ServiceImpl<ManagerInfoMapper, ManagerInfo>
        implements ManagerInfoService {


    @Autowired
    ManagerInfoMapper managerInfoMapper;

    @Override
    public void addManagerInfo(List<ManagerInfo> list) {

        managerInfoMapper.addManagerInfo(list);
    }

    @Override
    public List<ManagerInfo> queryManagerInfo() {
        return managerInfoMapper.queryManagerInfo();
    }

}




