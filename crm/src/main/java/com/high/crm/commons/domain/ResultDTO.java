package com.high.crm.commons.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname ReturnObject
 * @Description 响应信息通用类
 * @Author high
 * @Create 2022/10/30 14:54
 * @Version 1.0
 */
public class ResultDTO {
    // 处理状态码，0失败，1成功
    private String code;
    // 提示信息
    private String message;
    // 返回数据
    private Object data;

    public ResultDTO() {
    }

    public ResultDTO(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
