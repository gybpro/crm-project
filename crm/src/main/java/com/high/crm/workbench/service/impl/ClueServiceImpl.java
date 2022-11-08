package com.high.crm.workbench.service.impl;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.commons.util.RemarkConvertUtil;
import com.high.crm.commons.util.UUIDUtil;
import com.high.crm.settings.domain.User;
import com.high.crm.workbench.domain.*;
import com.high.crm.workbench.mapper.*;
import com.high.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname ClueServiceImpl
 * @Description 线索相关业务实现类
 * @Author high
 * @Create 2022/11/4 10:48
 * @Version 1.0
 */
@Service
public class ClueServiceImpl implements ClueService {
    private final ClueMapper clueMapper;

    private final CustomerMapper customerMapper;

    private final ContactsMapper contactsMapper;

    private final ClueRemarkMapper clueRemarkMapper;

    private final CustomerRemarkMapper customerRemarkMapper;

    private final ContactsRemarkMapper contactsRemarkMapper;

    private final ClueActivityRelationMapper clueActivityRelationMapper;

    private final ContactsActivityRelationMapper contactsActivityRelationMapper;

    private final TranMapper tranMapper;

    private final TranRemarkMapper tranRemarkMapper;

    public ClueServiceImpl(ClueMapper clueMapper, CustomerMapper customerMapper, ContactsMapper contactsMapper, ClueRemarkMapper clueRemarkMapper, CustomerRemarkMapper customerRemarkMapper, ContactsRemarkMapper contactsRemarkMapper, ClueActivityRelationMapper clueActivityRelationMapper, ContactsActivityRelationMapper contactsActivityRelationMapper, TranMapper tranMapper, TranRemarkMapper tranRemarkMapper) {
        this.clueMapper = clueMapper;
        this.customerMapper = customerMapper;
        this.contactsMapper = contactsMapper;
        this.clueRemarkMapper = clueRemarkMapper;
        this.customerRemarkMapper = customerRemarkMapper;
        this.contactsRemarkMapper = contactsRemarkMapper;
        this.clueActivityRelationMapper = clueActivityRelationMapper;
        this.contactsActivityRelationMapper = contactsActivityRelationMapper;
        this.tranMapper = tranMapper;
        this.tranRemarkMapper = tranRemarkMapper;
    }

    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> selectClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int selectCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public Clue selectClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    @Transactional
    public void convertClue(Map<String, Object> map) {
        // 获取用户信息和线索信息
        User user = (User) map.get(Constant.SESSION_USER);
        Clue clue = clueMapper.selectClueForDetailById((String) map.get("clueId"));

        // 封装客户对象
        Customer customer = new Customer();
        customer.setId(UUIDUtil.getUUID());
        customer.setName(clue.getCompany());
        customer.setOwner(clue.getOwner());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtil.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());
        // 添加客户信息
        customerMapper.insertCustomer(customer);

        // 封装联系人信息
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullName(clue.getFullName());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setmPhone(clue.getmPhone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtil.formatDateTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        // 添加联系人信息
        contactsMapper.insertContacts(contacts);

        // 获取线索备注信息
        List<Remark> remarkList = clueRemarkMapper.selectClueRemarkByClueId(clue.getId());
        // 遍历添加备注信息
        List<Remark> customerRemarkList = new ArrayList<>();
        List<Remark> contactsRemarkList = new ArrayList<>();
        for (Remark clueRemark : remarkList) {
            // 添加客户备注信息
            CustomerRemark customerRemark = new CustomerRemark();
            RemarkConvertUtil.remarkConvert(clueRemark, customerRemark);
            customerRemark.setCustomerId(customer.getId());
            customerRemarkList.add(customerRemark);
            // 添加联系人备注信息
            ContactsRemark contactsRemark = new ContactsRemark();
            RemarkConvertUtil.remarkConvert(clueRemark, contactsRemark);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemarkList.add(contactsRemark);
        }
        customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
        contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);

        // 查询关联的市场活动
        List<ClueActivityRelation> clueActivityRelationList =
                clueActivityRelationMapper.selectClueActivityRelationByClueId(clue.getId());
        // 遍历线索市场活动关联关系，将市场活动与联系人关联
        List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelationList.add(contactsActivityRelation);
        }
        contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);

        // 判断是否创建交易
        if ("true".equals(map.get("isCreateTran"))) {
            // 添加交易
            Tran tran = new Tran();
            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setCustomerId(customer.getId());
            tran.setStage((String) map.get("stage"));
            tran.setSource(clue.getSource());
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtil.formatDateTime(new Date()));
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());
            tranMapper.insertTran(tran);

            // 添加交易备注
            List<Remark> tranRemarkList = new ArrayList<>();
            for (Remark clueRemark : remarkList) {
                TranRemark tranRemark = new TranRemark();
                RemarkConvertUtil.remarkConvert(clueRemark, tranRemark);
                tranRemarkList.add(tranRemark);
            }
            tranRemarkMapper.insertTranRemarkByList(tranRemarkList);
        }

        // 删除线索
        clueMapper.deleteClueById(clue.getId());
        // 删除线索备注信息
        clueRemarkMapper.deleteClueRemarkByClueId(clue.getId());
        // 删除线索和市场活动的关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clue.getId());
    }
}
