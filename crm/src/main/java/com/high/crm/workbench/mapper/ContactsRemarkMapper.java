package com.high.crm.workbench.mapper;

import com.high.crm.workbench.domain.ContactsRemark;
import com.high.crm.workbench.domain.Remark;

import java.util.List;

public interface ContactsRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbg.generated Sun Nov 06 15:17:28 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbg.generated Sun Nov 06 15:17:28 CST 2022
     */
    int insert(ContactsRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbg.generated Sun Nov 06 15:17:28 CST 2022
     */
    int insertSelective(ContactsRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbg.generated Sun Nov 06 15:17:28 CST 2022
     */
    ContactsRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbg.generated Sun Nov 06 15:17:28 CST 2022
     */
    int updateByPrimaryKeySelective(ContactsRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbg.generated Sun Nov 06 15:17:28 CST 2022
     */
    int updateByPrimaryKey(ContactsRemark row);

    /**
     * 批量添加联系人备注信息
     * @param list
     * @return
     */
    int insertContactsRemarkByList(List<Remark> list);
}
