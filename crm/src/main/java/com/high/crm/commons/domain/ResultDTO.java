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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultDTO {
    // 处理状态码，0失败，1成功
    private String code;
    // 提示信息
    private String message;
    // 返回数据
    private Object data;
}
