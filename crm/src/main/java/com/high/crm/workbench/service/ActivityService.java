package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @Classname ActivityService
 * @Description 市场活动相关业务接口
 * @Author high
 * @Create 2022/11/1 10:01
 * @Version 1.0
 */
public interface ActivityService {
    /**
     * 添加创建的市场活动
     * @param activity
     * @return
     */
    public int insertActivity(Activity activity);

    /**
     * 按条件分页查询(全查，条件查，显示页，详情页)
     * @param map
     * @return
     */
    List<Activity> selectActivityByConditionForPage(Map<String, Object> map);

    /**
     * 按条件查询总条数
     * @param map
     * @return
     */
    int selectCountOfActivityByCondition(Map<String, Object> map);

    /**
     * 根据Ids批量删除市场活动
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids);
}
