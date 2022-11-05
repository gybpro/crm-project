package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.ClueActivityRelation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname ClueActivityRelationService
 * @Description 线索市场活动关联相关业务接口
 * @Author high
 * @Create 2022/11/4 22:23
 * @Version 1.0
 */
public interface ClueActivityRelationService {
    /**
     * 批量绑定线索和市场活动关系
     * @param relationList
     * @return
     */
    int insertClueActivityRelationByList(List<ClueActivityRelation> relationList);

    /**
     * 根据线索id和市场活动id删除线索与市场活动的关联
     * @param relation
     * @return
     */
    int deleteClueActivityRelationByActivityIdAndClueId(ClueActivityRelation relation);
}
