package com.high.crm.settings.service;

import com.high.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueService {
    List<DicValue> selectDicValueByTypeCode(String typeCode);
}
