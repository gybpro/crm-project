package com.high.crm.workbench.web.controller;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.domain.ResultDTO;
import com.high.crm.settings.domain.DicValue;
import com.high.crm.settings.domain.User;
import com.high.crm.settings.service.DicValueService;
import com.high.crm.settings.service.UserService;
import com.high.crm.workbench.domain.Remark;
import com.high.crm.workbench.domain.Tran;
import com.high.crm.workbench.domain.TranHistory;
import com.high.crm.workbench.service.CustomerService;
import com.high.crm.workbench.service.TranHistoryService;
import com.high.crm.workbench.service.TranRemarkService;
import com.high.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Classname TransactionController
 * @Description 交易相关控制
 * @Author high
 * @Create 2022/11/7 16:10
 * @Version 1.0
 */
@Controller
@RequestMapping("/workbench/transaction")
public class TransactionController {
    private final DicValueService dicValueService;

    private final UserService userService;

    private final CustomerService customerService;

    private final TranService tranService;

    private final TranRemarkService tranRemarkService;

    private final TranHistoryService tranHistoryService;

    public TransactionController(DicValueService dicValueService, UserService userService, CustomerService customerService, TranService tranService, TranRemarkService tranRemarkService, TranHistoryService tranHistoryService) {
        this.dicValueService = dicValueService;
        this.userService = userService;
        this.customerService = customerService;
        this.tranService = tranService;
        this.tranRemarkService = tranRemarkService;
        this.tranHistoryService = tranHistoryService;
    }

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询动态数据
        List<DicValue> stageList=dicValueService.selectDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList=dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.selectDicValueByTypeCode("source");
        //把数据保存到request
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/transaction/index";
    }

    @RequestMapping("/toSave.do")
    public String toSave(HttpServletRequest request){
        //调用service层方法，查询动态数据
        List<User> userList=userService.selectAllUser();
        List<DicValue> stageList=dicValueService.selectDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList=dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.selectDicValueByTypeCode("source");
        //把数据保存到request中
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/transaction/save";
    }

    @RequestMapping("/getPossibilityByStage.do")
    public @ResponseBody Object getPossibilityByStage(String stageValue){
        //解析properties配置文件，根据阶段获取可能性
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        //返回响应信息
        return bundle.getString(stageValue);
    }

    @RequestMapping("/queryCustomerNameByName.do")
    public @ResponseBody Object queryCustomerNameByName(String customerName){
        //调用service层方法，查询所有客户名称
        //根据查询结果，返回响应信息
        return customerService.selectCustomerNameByName(customerName);//['xxxx','xxxxx',......]
    }

    @RequestMapping("/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        //封装参数
        map.put(Constant.SESSION_USER,session.getAttribute(Constant.SESSION_USER));

        ResultDTO resultDTO=new ResultDTO();
        try {
            //调用service层方法，保存创建的交易
            tranService.insertTran(map);

            resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试....");
        }

        return resultDTO;
    }

    @RequestMapping("/detailTran.do")
    public String detailTran(String id,HttpServletRequest request){
        //调用service层方法，查询数据
        Tran tran=tranService.selectTranForDetailById(id);
        List<Remark> remarkList=tranRemarkService.selectTranRemarkForDetailByTranId(id);
        List<TranHistory> historyList=tranHistoryService.selectTranHistoryForDetailByTranId(id);

        //根据tran所处阶段名称查询可能性
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(tran.getStage());
        tran.setPossibility(possibility);

        //把数据保存到request中
        request.setAttribute("tran",tran);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("historyList",historyList);
        //request.setAttribute("possibility",possibility);

        //调用service方法，查询交易所有的阶段
        List<DicValue> stageList=dicValueService.selectDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);

        //请求转发
        return "workbench/transaction/detail";
    }
}
