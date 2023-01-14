package com.hc.my.common.core.util;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelExportUtils {

    public static final String EQUIPMENT_DATA_CUSTOM = "设备数据自定义查询";
    public static final String EQUIPMENT_DATA_POINT_IN_TIME = "设备数据时间点查询";

    public static final String ALARM_DATA_SUMMARY = "报警数据报警汇总查询";
    public static final String ALARM_DATA_NOTICE = "报警数据报警通知查询";
    public static final String ALARM_DATA_TIMEOUT = "报警数据超时数据查询";

    public static final String SYSTEM_DATA_HEARTBEAT = "系统数据丢包率查询";

    public static final String SYSTEM_LOG_OPERATION = "系统日志操作信息";


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

    public static List<ExcelExportEntity> getDatePoint(List<Date> dateList,boolean isCh) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if(isCh){
            beanList.add(new ExcelExportEntity("查询日期","date"));
            dateList.forEach(res->{
                String hHmm = DateUtils.dateReduceHHmm(res);
                beanList.add(new ExcelExportEntity(hHmm,hHmm));
            });
        }else {
            beanList.add(new ExcelExportEntity("query date","date"));
            dateList.forEach(res->{
                String hHmm = DateUtils.dateReduceHHmm(res);
                beanList.add(new ExcelExportEntity(hHmm,hHmm));
            });
        }
        return beanList;
    }

    public static List<ExcelExportEntity> getAlarmData(boolean isCh) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        beanList.add(new ExcelExportEntity("设备类型","equipmentTypeName"));
        beanList.add(new ExcelExportEntity("设备名称","equipmentName"));
        beanList.add(new ExcelExportEntity("设备报警次数","num"));
        return beanList;
    }

}
