package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.ClueActivityRelation;
import com.high.crm.workbench.mapper.ClueActivityRelationMapper;
import com.high.crm.workbench.service.ClueActivityRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname ClueActivityRelationServiceImpl
 * @Description 线索市场活动关联相关业务实现类
 * @Author high
 * @Create 2022/11/4 22:24
 * @Version 1.0
 */
@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    private final ClueActivityRelationMapper clueActivityRelationMapper;

    public ClueActivityRelationServiceImpl(ClueActivityRelationMapper clueActivityRelationMapper) {
        this.clueActivityRelationMapper = clueActivityRelationMapper;
    }

    @Override
    public int insertClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationMapper.insertClueActivityRelationByList(clueActivityRelationList);
    }
}
