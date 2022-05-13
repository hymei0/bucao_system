package com.example.bucao_springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.bucao_springboot.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@Api(tags = "文件上传和下载")
@RequestMapping("/files")
public class FileController{
    //获取端口号
    @Value("${server.port}")
    private String port;

    private static final String ip="http://localhost";

    /**
     * 上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "文件上传接口",notes="文件上传")
    @PostMapping("/upload")
    public Result<?>upload(@ApiParam(value = "选择文件") @Valid @RequestPart(value = "file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 获取源文件的名称
        if(originalFilename.equals(""))
        {
            return Result.error("-1","请上传文件");
        }

        // 定义文件的唯一标识（前缀）
        String flag = IdUtil.fastSimpleUUID();
        //获取项目路径
        String projectLocation = System.getProperty("user.dir");
        String rootFilePath = projectLocation  + "/bucao_springboot/src/main/resources/files/" + flag + "_" + originalFilename;  // 获取上传的路径
        //工具类：hutool
        FileUtil.writeBytes(file.getBytes(), rootFilePath);  // 把文件写入到上传的路径

        return Result.success( ip + ":" + port + "/files/" + flag);  // 返回结果 url
    }
    /**
     * 下载接口
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}")
    @ApiOperation(value = "文件下载接口",notes="文件下载")
    public Result<?> getFiles(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;  // 新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/bucao_springboot/src/main/resources/files/";  // 定于文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(basePath);  // 获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  // 找到跟参数一致的文件

        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);  // 通过文件的路径读取文件字节流
                os = response.getOutputStream();   // 通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
                return  Result.success();
            }else {
                return Result.error("-1","该文件不存在");
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
            return Result.error("-1","读取文件时出错");
        }
    }



}
