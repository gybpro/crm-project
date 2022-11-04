package com.high.crm.workbench.web.controller;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.domain.ResultDTO;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.commons.util.UUIDUtil;
import com.high.crm.settings.domain.DicValue;
import com.high.crm.settings.domain.User;
import com.high.crm.settings.service.DicValueService;
import com.high.crm.settings.service.UserService;
import com.high.crm.workbench.domain.Clue;
import com.high.crm.workbench.domain.ClueRemark;
import com.high.crm.workbench.service.ClueRemarkService;
import com.high.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname ClueController
 * @Description 线索相关控制
 * @Author high
 * @Create 2022/11/4 10:50
 * @Version 1.0
 */
@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    private final UserService userService;

    private final ClueService clueService;

    private final ClueRemarkService clueRemarkService;

    private final DicValueService dicValueService;

    public ClueController(UserService userService, ClueService clueService, ClueRemarkService clueRemarkService, DicValueService dicValueService) {
        this.userService = userService;
        this.clueService = clueService;
        this.clueRemarkService = clueRemarkService;
        this.dicValueService = dicValueService;
    }

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.selectAllUser();
        List<DicValue> appellationList=dicValueService.selectDicValueByTypeCode("appellation");
        List<DicValue> clueStateList=dicValueService.selectDicValueByTypeCode("clueState");
        List<DicValue> sourceList=dicValueService.selectDicValueByTypeCode("source");
        //把数据保存到request中
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }

    @RequestMapping("/insert.do")
    @ResponseBody
    public ResultDTO insert(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        // 封装参数
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateUtil.formatDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ResultDTO resultDTO = new ResultDTO();
        try {
            // 调用Service层方法，保存创建的市场活动
            int ret = clueService.insertClue(clue);
            if (ret > 0) {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
                resultDTO.setMessage("添加成功");
            } else {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
                resultDTO.setMessage("系统忙，请稍后重试......");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试......");
        }
        return resultDTO;
    }

    @RequestMapping("/select.do")
    @ResponseBody
    public ResultDTO select(String name, String owner, String company, String phone, String m_phone, String state, String source, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("company", company);
        map.put("phone", phone);
        map.put("m_phone", m_phone);
        map.put("state", state);
        map.put("source", source);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Clue> clueList = clueService.selectClueByConditionForPage(map);
        int totalRows = clueService.selectCountOfClueByCondition(map);
        ResultDTO resultDTO = new ResultDTO();
        if (clueList != null && !(clueList.isEmpty())) {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
            resultDTO.setMessage("查询成功");
            map.clear();
            map.put("clueList", clueList);
            map.put("totalRows", totalRows);
            resultDTO.setData(map);
        } else {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试......");
        }
        return resultDTO;
    }

    @RequestMapping("/detail.do")
    public String detail(String id, HttpServletRequest request) {
        Clue clue = clueService.selectClueForDetailById(id);
        List<ClueRemark> remarkList = clueRemarkService.selectClueRemarkByClueId(id);
        request.setAttribute("clue", clue);
        request.setAttribute("remarkList", remarkList);
        return "workbench/clue/detail";
    }
}
