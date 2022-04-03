package com.example.bucao_springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bucao_springboot.entity.Section;
import com.example.bucao_springboot.mapper.SectionMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/section")
public class SectionController {
    @Resource
    SectionMapper sectionMapper;

    //与Bucao_info表交互的接口
    @GetMapping("/rfid_kinds")
    public List<Map<String, Object>> SeletToRfidKinds()
    {
        QueryWrapper<Section> queryWrapper = new QueryWrapper<>();
        List<Map<String, Object>> list=sectionMapper.selectMaps(queryWrapper);
        return list;
    }
}
