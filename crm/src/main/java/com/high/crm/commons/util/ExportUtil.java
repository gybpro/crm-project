package com.high.crm.commons.util;

import com.high.crm.workbench.domain.Activity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Classname ExportUtil
 * @Description 导出数据工具类
 * @Author high
 * @Create 2022/11/3 9:12
 * @Version 1.0
 */
public class ExportUtil {
    /**
     * 导出市场活动为.xls的excel文件
     * @param activityList
     * @param response
     * @throws IOException
     */
    public static void exportActivity(List<Activity> activityList, HttpServletResponse response) throws IOException {
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
}
