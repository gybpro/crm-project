package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> selectTranHistoryForDetailByTranId(String tranId);
}
