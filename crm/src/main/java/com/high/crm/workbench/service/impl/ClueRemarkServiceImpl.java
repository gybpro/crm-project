package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.ClueRemark;
import com.high.crm.workbench.domain.Remark;
import com.high.crm.workbench.mapper.ClueRemarkMapper;
import com.high.crm.workbench.service.ClueRemarkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname ClueRemarkServiceImpl
 * @Description 线索备注信息相关业务实现类
 * @Author high
 * @Create 2022/11/4 14:33
 * @Version 1.0
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    private final ClueRemarkMapper clueRemarkMapper;

    public ClueRemarkServiceImpl(ClueRemarkMapper clueRemarkMapper) {
        this.clueRemarkMapper = clueRemarkMapper;
    }

    @Override
    public List<Remark> selectClueRemarkByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkByClueId(clueId);
    }
}
