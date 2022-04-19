package com.example.bucao_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bucao_springboot.entity.Bucao_user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper

public interface Bucao_userMapper extends BaseMapper<Bucao_user> {
    /**
     * 但病人办理入住、出院、bucao_room表添加、删除记录时需要及时更新布草的状态
     * @param rfno
     * @param rfid
     * @param state
     */
    @Update("update bucao_info set state=#{state} where rfno=#{rfno} and rfid=#{rfid}")
    void updatebucao(String rfno, String rfid,String state);

    /**
     * 选择布草分配不足6件的病人
     * @return
     */
    @Select("select * from user_info where id in(select user_id from bucao_user group by user_id having count(user_id)<4)")
    List<Map<String, Object>> selectforbucao();
}
