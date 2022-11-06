package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.Activity;
import com.high.crm.workbench.mapper.ActivityMapper;
import com.high.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public List<Activity> selectActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int selectCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int updateActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public List<Activity> selectAllActivity() {
        return activityMapper.selectAllActivity();
    }

    @Override
    public List<Activity> selectActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }

    @Override
    public int insertActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    @Override
    public Activity selectActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }

    @Override
    public List<Activity> selectActivityByClueId(String clueId) {
        return activityMapper.selectActivityByClueId(clueId);
    }

    @Override
    public List<Activity> selectActivityByNameAndClueId(Map<String, Object> map) {
        return activityMapper.selectActivityByNameAndClueId(map);
    }

    @Override
    public List<Activity> selectActivityForConvertByNameClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForConvertByNameClueId(map);
    }
}
