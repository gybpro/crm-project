package com.high.crm.workbench.mapper;

import com.high.crm.workbench.domain.Clue;
import com.high.crm.workbench.domain.ClueRemark;
import com.high.crm.workbench.domain.Remark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Fri Nov 04 14:27:01 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Fri Nov 04 14:27:01 CST 2022
     */
    int insert(ClueRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Fri Nov 04 14:27:01 CST 2022
     */
    int insertSelective(ClueRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Fri Nov 04 14:27:01 CST 2022
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Fri Nov 04 14:27:01 CST 2022
     */
    int updateByPrimaryKeySelective(ClueRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Fri Nov 04 14:27:01 CST 2022
     */
    int updateByPrimaryKey(ClueRemark row);

    /**
     * 根据线索id查询备注信息
     * @param clueId
     * @return
     */
    List<Remark> selectClueRemarkByClueId(String clueId);

    /**
     * 根据线索id删除线索备注信息
     * @param clueId
     * @return
     */
    int deleteClueRemarkByClueId(String clueId);
}
