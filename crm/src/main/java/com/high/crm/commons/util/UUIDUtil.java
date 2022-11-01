package com.high.crm.commons.util;

import java.util.UUID;

/**
 * @Classname UUIDUtil
 * @Description UUID生成工具类
 * @Author high
 * @Create 2022/11/1 10:22
 * @Version 1.0
 */
public class UUIDUtil {
    /**
     * 获取UUID值字符串
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
