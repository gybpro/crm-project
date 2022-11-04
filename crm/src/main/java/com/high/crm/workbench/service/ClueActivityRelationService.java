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
     * @param clueActivityRelationList
     * @return
     */
    int insertClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList);
}
