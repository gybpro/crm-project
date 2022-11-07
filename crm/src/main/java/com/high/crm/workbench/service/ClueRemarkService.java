package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.ClueRemark;
import com.high.crm.workbench.domain.Remark;

import java.util.List;

/**
 * @Classname ClueRemarkService
 * @Description 线索备注信息相关业务接口
 * @Author high
 * @Create 2022/11/4 14:32
 * @Version 1.0
 */
public interface ClueRemarkService {
    /**
     * 根据线索id查询备注信息
     * @param clueId
     * @return
     */
    List<Remark> selectClueRemarkByClueId(String clueId);
}
