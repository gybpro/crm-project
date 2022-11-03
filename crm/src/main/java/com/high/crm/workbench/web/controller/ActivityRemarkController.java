package com.high.crm.workbench.web.controller;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.domain.ResultDTO;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.commons.util.UUIDUtil;
import com.high.crm.settings.domain.User;
import com.high.crm.workbench.domain.ActivityRemark;
import com.high.crm.workbench.service.ActivityRemarkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @Classname ActivityRemarkController
 * @Description 市场活动备注信息相关控制
 * @Author high
 * @Create 2022/11/3 20:46
 * @Version 1.0
 */
@Controller
@RequestMapping("/workbench/activity")
public class ActivityRemarkController {
    private final ActivityRemarkService activityRemarkService;

    public ActivityRemarkController(ActivityRemarkService activityRemarkService) {
        this.activityRemarkService = activityRemarkService;
    }

    @RequestMapping("/insertRemark.do")
    @ResponseBody
    public ResultDTO insertRemark(String noteContent, String activityId, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        ActivityRemark remark = new ActivityRemark();
        remark.setId(UUIDUtil.getUUID());
        remark.setNoteContent(noteContent);
        remark.setCreateTime(DateUtil.formatDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag("0");
        remark.setActivityId(activityId);
        ResultDTO resultDTO = new ResultDTO();
        if (activityRemarkService.insertActivityRemark(remark) > 0) {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
            resultDTO.setMessage("添加成功");
            resultDTO.setData(remark);
        } else {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试......");
        }
        return resultDTO;
    }

    @RequestMapping("/deleteRemarkById.do")
    @ResponseBody
    public ResultDTO deleteRemarkById(String id) {
        ResultDTO resultDTO = new ResultDTO();
        if (activityRemarkService.deleteActivityRemarkById(id) > 0) {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_SUCCESS);
            resultDTO.setMessage("删除成功");
        } else {
            resultDTO.setCode(Constant.RESULT_DTO_CODE_FAIL);
            resultDTO.setMessage("系统忙，请稍后重试......");
        }
        return resultDTO;
    }
}
