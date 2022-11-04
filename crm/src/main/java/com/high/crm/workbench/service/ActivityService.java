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

    /**
     * 根据id查询相应信息
     * @param id
     * @return
     */
    Activity selectActivityById(String id);

    /**
     * 修改市场活动信息
     * @param activity
     * @return
     */
    int updateActivity(Activity activity);

    /**
     * 查询所有信息
     * @return
     */
    List<Activity> selectAllActivity();

    /**
     * 根据id批量查询信息
     * @param ids
     * @return
     */
    List<Activity> selectActivityByIds(String[] ids);

    /**
     * 批量添加市场活动
     * @param activityList
     * @return
     */
    int insertActivityByList(List<Activity> activityList);

    /**
     * 查询市场活动详细信息
     * @param id
     * @return
     */
    Activity selectActivityForDetailById(String id);

    /**
     * 根据线索id查询市场活动
     * @param clueId
     * @return
     */
    List<Activity> selectActivityByClueId(String clueId);

    /**
     * 根据市场活动名和线索id查询市场活动，模糊查询加排除已关联市场活动
     * @param map
     * @return
     */
    List<Activity> selectActivityByNameAndClueId(Map<String, Object> map);
}
