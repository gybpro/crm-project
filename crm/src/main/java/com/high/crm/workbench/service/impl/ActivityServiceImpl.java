package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.Activity;
import com.high.crm.workbench.mapper.ActivityMapper;
import com.high.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

/**
 * @Classname ActivityServiceImpl
 * @Description 市场活动相关业务实现类
 * @Author high
 * @Create 2022/11/1 10:01
 * @Version 1.0
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    @Override
    public int insertActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }
}
