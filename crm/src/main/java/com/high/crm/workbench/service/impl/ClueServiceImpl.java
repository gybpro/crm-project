package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.Clue;
import com.high.crm.workbench.mapper.ClueMapper;
import com.high.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname ClueServiceImpl
 * @Description 线索相关业务实现类
 * @Author high
 * @Create 2022/11/4 10:48
 * @Version 1.0
 */
@Service
public class ClueServiceImpl implements ClueService {
    private final ClueMapper clueMapper;

    public ClueServiceImpl(ClueMapper clueMapper) {
        this.clueMapper = clueMapper;
    }

    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> selectClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int selectCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }
}
