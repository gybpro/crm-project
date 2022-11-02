package com.high.crm.workbench.web.controller;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.domain.ResultDTO;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.commons.util.UUIDUtil;
import com.high.crm.settings.domain.User;
import com.high.crm.settings.service.UserService;
import com.high.crm.workbench.domain.Activity;
import com.high.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname ActivityController
 * @Description 市场活动界面控制
 * @Author high
 * @Create 2022/10/31 21:41
 * @Version 1.0
 */
@Controller
@RequestMapping("/workbench/activity")
public class ActivityController extends HttpServlet {
    private final UserService userService;

    private final ActivityService activityService;

    public ActivityController(UserService userService, ActivityService activityService) {
        this.userService = userService;
        this.activityService = activityService;
    }

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.selectAllUser();
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/insert.do")
    @ResponseBody
    public ResultDTO insert(Activity activity, HttpSession session) {
        System.out.println(activity.getName());
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        // 封装参数
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateUtil.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ResultDTO resultDTO = new ResultDTO();
        try {
            // 调用Service层方法，保存创建的市场活动
            int ret = activityService.insertActivity(activity);
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
    public ResultDTO select(String name, String owner, String startDate, String endDate, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Activity> activityList = activityService.selectActivityByConditionForPage(map);
        int totalRows = activityService.selectCountOfActivityByCondition(map);
        ResultDTO resultDTO = new ResultDTO();
        if (activityList != null && !(activityList.isEmpty())) {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
            resultDTO.setMessage("查询成功");
            map.clear();
            map.put("activityList", activityList);
            map.put("totalRows", totalRows);
            resultDTO.setData(map);
        } else {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试......");
        }
        return resultDTO;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public ResultDTO delete(String[] id) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (activityService.deleteActivityByIds(id) > 0) {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
                resultDTO.setMessage("删除成功");
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

    @RequestMapping("selectOne.do")
    @ResponseBody
    public ResultDTO selectOne(String id) {
        Activity activity = activityService.selectActivityById(id);
        ResultDTO resultDTO = new ResultDTO();
        if (activity != null) {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
            resultDTO.setMessage("查询成功");
            resultDTO.setData(activity);
        } else {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试......");
        }
        return resultDTO;
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public ResultDTO update(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        activity.setEditTime(DateUtil.formatDateTime(new Date()));
        activity.setEditBy(user.getId());
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (activityService.updateActivity(activity) > 0) {
                resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
                resultDTO.setMessage("修改成功");
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

    @RequestMapping("/detail.do")
    public String detail() {
        return "workbench/activity/detail";
    }
}
