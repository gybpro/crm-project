package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.ActivityRemark;
import com.high.crm.workbench.mapper.ActivityRemarkMapper;
import com.high.crm.workbench.service.ActivityRemarkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname ActivityRemarkServiceImpl
 * @Description 市场活动备注信息相关业务实现类
 * @Author high
 * @Create 2022/11/3 19:35
 * @Version 1.0
 */
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    private final ActivityRemarkMapper activityRemarkMapper;

    public ActivityRemarkServiceImpl(ActivityRemarkMapper activityRemarkMapper) {
        this.activityRemarkMapper = activityRemarkMapper;
    }

    @Override
    public List<ActivityRemark> selectActivityRemarkByActivityId(String activityId) {
        return activityRemarkMapper.selectActivityRemarkByActivityId(activityId);
    }

    @Override
    public int insertActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.insertActivityRemark(remark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }

    @Override
    public int updateActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.updateActivityRemark(remark);
    }
}
