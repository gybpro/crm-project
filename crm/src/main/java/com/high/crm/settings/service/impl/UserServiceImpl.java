package com.high.crm.settings.service.impl;

import com.high.crm.settings.domain.User;
import com.high.crm.settings.mapper.UserMapper;
import com.high.crm.settings.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Classname UserServiceImpl
 * @Description 用户相关业务实现类
 * @Author high
 * @Create 2022/10/30 10:52
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User selectUserByLoginActAndLoginPwd(Map<String, String> map) {
        return userMapper.selectUserByLoginActAndLoginPwd(map);
    }
}
