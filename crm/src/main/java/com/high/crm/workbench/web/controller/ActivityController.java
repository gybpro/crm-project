package com.high.crm.workbench.web.controller;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.domain.ResultDTO;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.commons.util.UUIDUtil;
import com.high.crm.settings.domain.User;
import com.high.crm.settings.service.UserService;
import com.high.crm.workbench.domain.Activity;
import com.high.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
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

    @RequestMapping("/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws IOException {
        // 调用Service层方法，查询所有市场活动
        List<Activity> activityList = activityService.selectAllActivity();
        // 创建excel文件，把数据写入文件中
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        // 遍历activityList，创建HSSFRow对象，生成所有的数据行
        if (activityList != null && activityList.size() > 0) {
            Activity activity;
            for (int i = 0; i < activityList.size(); i++) {
                // 获取元素
                activity = activityList.get(i);
                // 生成行装对象数据
                row = sheet.createRow(i + 1);
                // 装载数据
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        // 把生成的文件下载到客户端
        // 设置内容类型为二进制流文件
        response.setContentType("application/octet-stream;charset=utf-8");
        // 设置响应头的处置方式为附件，并设置默认命名
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");
        // 获取响应输出流
        OutputStream out = response.getOutputStream();

        // 将文件写入流
        wb.write(out);

        // 关闭资源
        wb.close();
        // tomcat的流不能关，要由tomcat控制，否则可能传输未完成流就关闭
        // 也可能tomcat还要使用，如果关闭会出现严重错误
        // 将流中数据全部强制输出，防止数据残留
        out.flush();
    }

    @RequestMapping("/detail.do")
    public String detail() {
        return "workbench/activity/detail";
    }
}
