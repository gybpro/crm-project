package com.high.crm.workbench.service;

import com.high.crm.workbench.domain.Activity;
import com.high.crm.workbench.domain.Clue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname ClueService
 * @Description 线索相关业务接口
 * @Author high
 * @Create 2022/11/4 10:47
 * @Version 1.0
 */
public interface ClueService {
    /**
     * 添加创建的线索
     * @param clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * 按条件分页查询(全查，条件查，显示页，详情页)
     * @param map
     * @return
     */
    List<Clue> selectClueByConditionForPage(Map<String, Object> map);

    /**
     * 按条件查询总条数
     * @param map
     * @return
     */
    int selectCountOfClueByCondition(Map<String, Object> map);

    /**
     * 根据id查询线索详细信息
     * @param id
     * @return
     */
    Clue selectClueForDetailById(String id);

    /**
     * 完成线索转换业务
     * @param map
     * @return
     */
    void convertClue(Map<String, Object> map);
}
