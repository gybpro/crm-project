package com.high.crm.settings.mapper;

import com.high.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Sun Oct 30 16:56:40 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Sun Oct 30 16:56:40 CST 2022
     */
    int insert(User row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Sun Oct 30 16:56:40 CST 2022
     */
    int insertSelective(User row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Sun Oct 30 16:56:40 CST 2022
     */
    User selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Sun Oct 30 16:56:40 CST 2022
     */
    int updateByPrimaryKeySelective(User row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Sun Oct 30 16:56:40 CST 2022
     */
    int updateByPrimaryKey(User row);

    User selectUserByLoginActAndLoginPwd(Map<String, String> map);

    List<User> selectAllUser();
}
