package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.Remark;

import java.util.List;

public interface TranRemarkService {
    List<Remark> selectTranRemarkForDetailByTranId(String tranId);
}
