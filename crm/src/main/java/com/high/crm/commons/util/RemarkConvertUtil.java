package com.high.crm.commons.util;

import com.high.crm.workbench.domain.Remark;

/**
 * @Classname RemarkConvertUtil
 * @Description 备注信息转换工具
 * @Author high
 * @Create 2022/11/7 10:14
 * @Version 1.0
 */
public class RemarkConvertUtil {
    public static void remarkConvert(Remark source, Remark target) {
        target.setId(UUIDUtil.getUUID());

        target.setNoteContent(source.getNoteContent());

        target.setCreateBy(source.getCreateBy());

        target.setCreateTime(source.getCreateTime());

        target.setEditBy(source.getEditBy());

        target.setEditTime(source.getEditTime());

        target.setEditFlag(source.getEditFlag());
    }
}
