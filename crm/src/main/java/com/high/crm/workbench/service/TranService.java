package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.FunnelVO;
import com.high.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * @Classname TranService
 * @Description 交易相关业务接口
 * @Author high
 * @Create 2022/11/7 16:11
 * @Version 1.0
 */
public interface TranService {
    /**
     * 按条件分页查询(全查，条件查，显示页，详情页)
     * @param map
     * @return
     */
    List<Tran> selectTranByConditionForPage(Map<String, Object> map);

    /**
     * 添加交易
     * @param map
     * @return
     */
    void insertTran(Map<String, Object> map);

    /**
     * 根据id查询交易的明细信息
     * @param id
     * @return
     */
    Tran selectTranForDetailById(String id);

    /**
     * 查询交易表中各个阶段的数据量
     * @return
     */
    List<FunnelVO> selectCountOfTranGroupByStage();
}
