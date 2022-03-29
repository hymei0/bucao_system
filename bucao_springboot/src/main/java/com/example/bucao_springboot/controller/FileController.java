package com.example.bucao_springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.example.bucao_springboot.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController{
    @PostMapping("/upload")
    public Result<?>upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 获取源文件的名称
        // 定义文件的唯一标识（前缀）
        String flag = IdUtil.fastSimpleUUID();
        //获取项目路径
        String projectLocation = System.getProperty("user.dir");
        String rootFilePath = projectLocation  + "/bucao_springboot/src/main/resources/files/" + flag + "_" + originalFilename;  // 获取上传的路径
        //工具类：hutool
        FileUtil.writeBytes(file.getBytes(), rootFilePath);  // 把文件写入到上传的路径

        return Result.success();  // 返回结果 url
    }

}
