package com.high.crm.workbench.service.impl;

import com.high.crm.workbench.mapper.CustomerMapper;
import com.high.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Override
    public List<String> selectCustomerNameByName(String name) {
        return customerMapper.selectCustomerNameByName(name);
    }
}
