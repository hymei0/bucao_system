package com.example.bucao_springboot.Service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bucao_springboot.Service.ManagerInfoService;
import com.example.bucao_springboot.entity.ManagerInfo;
import com.example.bucao_springboot.mapper.ManagerInfoMapper;
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
public class ManagerInfoServiceImpl implements ManagerInfoService {
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

    @Override
    public boolean saveBatch(Collection<ManagerInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<ManagerInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<ManagerInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(ManagerInfo entity) {
        return false;
    }

    @Override
    public ManagerInfo getOne(Wrapper<ManagerInfo> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<ManagerInfo> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<ManagerInfo> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<ManagerInfo> getBaseMapper() {
        return null;
    }

    @Override
    public Class<ManagerInfo> getEntityClass() {
        return null;
    }
}




