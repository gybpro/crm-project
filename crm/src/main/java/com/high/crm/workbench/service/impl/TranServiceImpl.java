package com.high.crm.workbench.service.impl;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.commons.util.UUIDUtil;
import com.high.crm.settings.domain.User;
import com.high.crm.workbench.domain.Customer;
import com.high.crm.workbench.domain.FunnelVO;
import com.high.crm.workbench.domain.Tran;
import com.high.crm.workbench.domain.TranHistory;
import com.high.crm.workbench.mapper.CustomerMapper;
import com.high.crm.workbench.mapper.TranHistoryMapper;
import com.high.crm.workbench.mapper.TranMapper;
import com.high.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname TranServiceImpl
 * @Description 交易相关业务接口
 * @Author high
 * @Create 2022/11/7 16:12
 * @Version 1.0
 */
@Service
public class TranServiceImpl implements TranService {
    private final TranMapper tranMapper;

    private final CustomerMapper customerMapper;

    private final TranHistoryMapper tranHistoryMapper;

    public TranServiceImpl(TranMapper tranMapper, CustomerMapper customerMapper, TranHistoryMapper tranHistoryMapper) {
        this.tranMapper = tranMapper;
        this.customerMapper = customerMapper;
        this.tranHistoryMapper = tranHistoryMapper;
    }

    @Override
    public List<Tran> selectTranByConditionForPage(Map<String, Object> map) {
        return new ArrayList<>();
    }

    @Override
    public void insertTran(Map<String, Object> map) {
        String customerName=(String) map.get("customerName");
        User user=(User) map.get(Constant.SESSION_USER);
        //根据name精确查询客户
        Customer customer=customerMapper.selectCustomerByName(customerName);
        //如果客户不存在，则新建客户
        if(customer==null){
            customer=new Customer();
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(DateUtil.formatDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customerMapper.insertCustomer(customer);
        }
        //保存创建的交易
        Tran tran=new Tran();
        tran.setStage((String) map.get("stage"));
        tran.setOwner((String) map.get("owner"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtil.getUUID());
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DateUtil.formatDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));
        tranMapper.insertTran(tran);
        //保存交易历史
        TranHistory tranHistory=new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtil.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public Tran selectTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> selectCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
