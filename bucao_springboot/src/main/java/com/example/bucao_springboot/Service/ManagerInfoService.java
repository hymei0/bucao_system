package com.example.bucao_springboot.Service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bucao_springboot.entity.ManagerInfo;

import java.util.List;

/**
* @author we
* @description 针对表【manager_info】的数据库操作Service
* @createDate 2022-04-01 22:34:15
*/
public interface ManagerInfoService extends IService<ManagerInfo> {
    //查询所有
    List<ManagerInfo> queryManagerInfo();
    //插入所有
    void addManagerInfo(List<ManagerInfo> list);

}
