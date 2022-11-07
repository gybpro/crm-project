package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.ActivityRemark;
import com.high.crm.workbench.domain.Remark;

import java.util.List;

/**
 * @Classname ActivityRemarkService
 * @Description 市场活动备注信息相关业务接口
 * @Author high
 * @Create 2022/11/3 19:35
 * @Version 1.0
 */
public interface ActivityRemarkService {
    /**
     * 根据市场活动id查询相关备注信息
     * @param activityId
     * @return
     */
    List<Remark> selectActivityRemarkByActivityId(String activityId);

    /**
     * 添加市场活动备注
     * @param remark
     * @return
     */
    int insertActivityRemark(ActivityRemark remark);

    /**
     * 根据id删除市场活动备注信息
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     * 修改市场活动备注信息功能
     * @param remark
     * @return
     */
    int updateActivityRemark(ActivityRemark remark);
}
