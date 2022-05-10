package com.example.bucao_springboot.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//返回给前台数据的包装类
@ApiModel(value = "返回结果的包装类")
public class Result<T> {
    @ApiModelProperty(value = "告诉前台返回的数据是成功（“1”）还是失败（“-1”）")
    private String code;//告诉前台返回的数据是成功（“1”）还是失败（“0”）
    @ApiModelProperty(value = "将错误信息返回给前台")
    private String msg; //将错误信息返回给前台，前台弹出
    @ApiModelProperty(value = "操作的返回的数据结果")
    private T data;
    public String getcode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code=code;
    }
    public String getMsg()
    {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(){}
    public Result(T data)
    {
        this.data=data;
    }

    public static Result success()
    {
        Result result = new Result<>();
        result.setCode("1");
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success(T data )
    {
        Result<T> result = new Result<>(data);
        result.setCode("1");
        result.setMsg("成功");
        return result;
    }
    public static Result error(String code ,String msg)
    {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
