package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.RoomInfo;
import generator.service.RoomInfoService;
import generator.mapper.RoomInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author we
* @description 针对表【room_info】的数据库操作Service实现
* @createDate 2022-04-05 14:19:23
*/
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
    implements RoomInfoService{

}




