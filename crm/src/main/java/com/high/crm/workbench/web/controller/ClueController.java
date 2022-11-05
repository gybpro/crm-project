package com.high.crm.workbench.web.controller;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.domain.ResultDTO;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.commons.util.UUIDUtil;
import com.high.crm.settings.domain.DicValue;
import com.high.crm.settings.domain.User;
import com.high.crm.settings.service.DicValueService;
import com.high.crm.settings.service.UserService;
import com.high.crm.workbench.domain.Activity;
import com.high.crm.workbench.domain.Clue;
import com.high.crm.workbench.domain.ClueActivityRelation;
import com.high.crm.workbench.domain.ClueRemark;
import com.high.crm.workbench.service.ActivityService;
import com.high.crm.workbench.service.ClueActivityRelationService;
import com.high.crm.workbench.service.ClueRemarkService;
import com.high.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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

    private final ActivityService activityService;

    private final ClueActivityRelationService clueActivityRelationService;

    public ClueController(UserService userService, ClueService clueService, ClueRemarkService clueRemarkService, DicValueService dicValueService, ActivityService activityService, ClueActivityRelationService clueActivityRelationService) {
        this.userService = userService;
        this.clueService = clueService;
        this.clueRemarkService = clueRemarkService;
        this.dicValueService = dicValueService;
        this.activityService = activityService;
        this.clueActivityRelationService = clueActivityRelationService;
    }

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.selectAllUser();
        List<DicValue> appellationList = dicValueService.selectDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.selectDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");
        //把数据保存到request中
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);
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
        List<Activity> activityList = activityService.selectActivityByClueId(id);
        request.setAttribute("clue", clue);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("activityList", activityList);
        return "workbench/clue/detail";
    }

    @RequestMapping("/selectActivityByNameAndClueId.do")
    @ResponseBody
    public Object selectActivityByNameAndClueId(String activityName, String clueId) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        //调用service层方法，查询市场活动;
        //根据查询结果，返回响应信息
        return activityService.selectActivityByNameAndClueId(map);
    }

    @RequestMapping("saveBind.do")
    @ResponseBody
    public ResultDTO saveBind(String[] activityId, String clueId) {
        //封装参数
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for (String ai : activityId) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtil.getUUID());
            relationList.add(car);
        }

        ResultDTO resultDTO = new ResultDTO();
        try {
            //调用service方法，批量保存线索和市场活动的关联关系
            if (clueActivityRelationService.insertClueActivityRelationByList(relationList) > 0) {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
                List<Activity> activityList = activityService.selectActivityByIds(activityId);
                resultDTO.setMessage("关联成功");
                resultDTO.setData(activityList);
            } else {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
                resultDTO.setMessage("系统忙，请稍后重试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试....");
        }
        return resultDTO;
    }

    @RequestMapping("/relieveBind.do")
    @ResponseBody
    public ResultDTO relieveBind(ClueActivityRelation relation) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (clueActivityRelationService.deleteClueActivityRelationByActivityIdAndClueId(relation) > 0) {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
                resultDTO.setMessage("删除成功");
            } else {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
                resultDTO.setMessage("系统忙，请稍后重试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试....");
        }
        return resultDTO;
    }
}
