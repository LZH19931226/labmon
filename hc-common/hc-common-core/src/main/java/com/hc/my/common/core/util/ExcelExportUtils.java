package com.hc.my.common.core.util;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;

import java.util.ArrayList;
import java.util.List;

public class ExcelExportUtils {

    public static final String ALARM_NOTICE = "报警通知数据汇总";
    public static final String ALARM_US_NOTICE = "ALARM_NOTICE_DATA";
    public static final String EQUIPMENT_DATA = "设备数据汇总";

    /**
     * 获取报警通知模板
     */
    public static List<ExcelExportEntity> getAlarmNoticeModel(){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        beanList.add(new ExcelExportEntity("数据记录时间","dataLoggingTime"));
        beanList.add(new ExcelExportEntity("通知人员","userName"));
        beanList.add(new ExcelExportEntity("通知的手机号","phoneNum"));
        beanList.add(new ExcelExportEntity("通知类型","publishType"));
        beanList.add(new ExcelExportEntity("通知状态","state"));
        beanList.add(new ExcelExportEntity("失败原因","fReason"));
        beanList.add(new ExcelExportEntity("邮件内容","mailContent"));
        return beanList;
    }

    /**
     * 获取英文报警通知模板
     */
    public static List<ExcelExportEntity> getUsAlarmNoticeModel(){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        beanList.add(new ExcelExportEntity("TIME","dataLoggingTime"));
        beanList.add(new ExcelExportEntity("NOTIFY_PEOPLE","userName"));
        beanList.add(new ExcelExportEntity("NOTIFY_PHONE","phoneNum"));
        beanList.add(new ExcelExportEntity("NOTIFY_TYPE","publishType"));
        beanList.add(new ExcelExportEntity("NOTIFY_STATE","state"));
        beanList.add(new ExcelExportEntity("FAIL_REASON","fReason"));
        beanList.add(new ExcelExportEntity("MAIL_CONTENT","mailContent"));
        return beanList;
    }

    public static List<ExcelExportEntity> getEquipmentData(String name,String field){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        beanList.add(new ExcelExportEntity("记录时间","inputdatetime"));
        beanList.add(new ExcelExportEntity(name,field));
        return beanList;
    }

    public static List<ExcelExportEntity> getPacketLossLog(boolean isCh){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if (isCh){
            beanList.add(new ExcelExportEntity("心跳包记录时间","inputdatetime"));
            beanList.add(new ExcelExportEntity("设备SN","sn"));
            beanList.add(new ExcelExportEntity("心跳包数据","remark1"));
        }else {
            beanList.add(new ExcelExportEntity("heartbeat_time","inputdatetime"));
            beanList.add(new ExcelExportEntity("sn_num","sn"));
            beanList.add(new ExcelExportEntity("heartbeat_data","remark1"));
        }

        return beanList;
    }

}
