package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.TranHistory;
import com.high.crm.workbench.mapper.TranHistoryMapper;
import com.high.crm.workbench.service.TranHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranHistoryService")
public class TranHistoryServiceImpl implements TranHistoryService {

    private final TranHistoryMapper tranHistoryMapper;

    public TranHistoryServiceImpl(TranHistoryMapper tranHistoryMapper) {
        this.tranHistoryMapper = tranHistoryMapper;
    }

    @Override
    public List<TranHistory> selectTranHistoryForDetailByTranId(String tranId) {
        return tranHistoryMapper.selectTranHistoryForDetailByTranId(tranId);
    }
}
