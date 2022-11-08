package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.domain.Remark;
import com.high.crm.workbench.domain.TranRemark;
import com.high.crm.workbench.mapper.TranRemarkMapper;
import com.high.crm.workbench.service.TranRemarkService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService {

    private final TranRemarkMapper tranRemarkMapper;

    public TranRemarkServiceImpl(TranRemarkMapper tranRemarkMapper) {
        this.tranRemarkMapper = tranRemarkMapper;
    }

    @Override
    public List<Remark> selectTranRemarkForDetailByTranId(String tranId) {
        return tranRemarkMapper.selectTranRemarkForDetailByTranId(tranId);
    }
}
