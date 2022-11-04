package com.high.crm.settings.service.impl;

import com.high.crm.settings.domain.DicValue;
import com.high.crm.settings.mapper.DicValueMapper;
import com.high.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {
    private final DicValueMapper dicValueMapper;

    public DicValueServiceImpl(DicValueMapper dicValueMapper) {
        this.dicValueMapper = dicValueMapper;
    }

    @Override
    public List<DicValue> selectDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
