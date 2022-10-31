package com.high.crm.settings.service;

import com.high.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @Classname UserService
 * @Description 用户相关业务接口
 * @Author high
 * @Create 2022/10/30 9:32
 * @Version 1.0
 */
public interface UserService {
    User selectUserByLoginActAndLoginPwd(Map<String, String> map);

    List<User> selectAllUser();
}
