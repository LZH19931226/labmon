package com.hc.my.common.core.util;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.struct.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelExportUtils {

    public static final String EQUIPMENT_DATA_CUSTOM = "设备数据自定义查询";
    public static final String EQUIPMENT_DATA_CUSTOM_US = "EQUIPMENT_DATA_CUSTOM";
    public static final String EQUIPMENT_DATA_POINT_IN_TIME = "设备数据时间点查询";
    public static final String EQUIPMENT_DATA_POINT_IN_TIME_US = "EQUIPMENT_DATA_POINT_IN_TIME";

    public static final String ALARM_DATA_SUMMARY = "报警数据报警汇总查询";
    public static final String ALARM_DATA_SUMMARY_US = "ALARM_DATA_SUMMARY";
    public static final String ALARM_DATA_NOTICE = "报警数据报警通知查询";
    public static final String ALARM_DATA_NOTICE_US = "ALARM_DATA_NOTICE";


    public static final String ALARM_DATA_TIMEOUT = "报警数据超时数据查询";

    public static final String SYSTEM_DATA_HEARTBEAT = "系统数据丢包率查询";
    public static final String SYSTEM_DATA_HEARTBEAT_US = "SYSTEM_DATA_HEARTBEAT";

    public static final String SYSTEM_LOG_OPERATION = "系统日志操作信息";
    public static final String SYSTEM_LOG_OPERATION_US = "SYSTEM_LOG_OPERATION";

    /**
     * 获取设备数据自定义查询 返回string
     */
    public static String getEquipmentDataModel() {
        String model = "";
        if (Context.IsCh()) {
            model = EQUIPMENT_DATA_CUSTOM;
        } else {
            model = EQUIPMENT_DATA_CUSTOM_US;
        }
        return model;
    }

    /**
     * 获取设备数据时间点查询 返回string
     */
    public static String getEquipmentDataPointInTimeModel() {
        String model = "";
        if (Context.IsCh()) {
            model = EQUIPMENT_DATA_POINT_IN_TIME;
        } else {
            model = EQUIPMENT_DATA_POINT_IN_TIME_US;
        }
        return model;
    }

    /**
     * 获取报警数据报警汇总查询 返回string
     */
    public static String getAlarmDataSummaryModel() {
        String model = "";
        if (Context.IsCh()) {
            model = ALARM_DATA_SUMMARY;
        } else {
            model = ALARM_DATA_SUMMARY_US;
        }
        return model;
    }

    /**
     * 获取报警数据报警通知查询 返回string
     */
    public static String getAlarmDataNoticeModel() {
        String model = "";
        if (Context.IsCh()) {
            model = ALARM_DATA_NOTICE;
        } else {
            model = ALARM_DATA_NOTICE_US;
        }
        return model;
    }

    /**
     * 获取系统数据丢包率查询 返回string
     */
    public static String getSystemDataHeartbeatModel() {
        String model = "";
        if (Context.IsCh()) {
            model = SYSTEM_DATA_HEARTBEAT;
        } else {
            model = SYSTEM_DATA_HEARTBEAT_US;
        }
        return model;
    }

    /**
     * 获取系统日志操作信息
     */
    public static String getSystemLogOperationModel() {
        String model = "";
        if (Context.IsCh()) {
            model = SYSTEM_LOG_OPERATION;
        } else {
            model = SYSTEM_LOG_OPERATION_US;
        }
        return model;
    }

    /**
     * 获取报警通知模板
     */
    public static List<ExcelExportEntity> getAlarmNoticeModel(boolean isCh){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if(isCh){
            beanList.add(new ExcelExportEntity("数据记录时间","dataLoggingTime"));
            beanList.add(new ExcelExportEntity("通知人员","userName"));
            beanList.add(new ExcelExportEntity("通知的手机号","phoneNum"));
            beanList.add(new ExcelExportEntity("通知类型","publishType"));
            beanList.add(new ExcelExportEntity("通知状态","state"));
            beanList.add(new ExcelExportEntity("失败原因","fReason"));
            beanList.add(new ExcelExportEntity("邮件内容","mailContent"));
        }else {
            beanList.add(new ExcelExportEntity("TIME","dataLoggingTime"));
            beanList.add(new ExcelExportEntity("NOTIFY_STAFF","userName"));
            beanList.add(new ExcelExportEntity("MOBILE_PHONE","phoneNum"));
            beanList.add(new ExcelExportEntity("NOTIFICATION_TYPE","publishType"));
            beanList.add(new ExcelExportEntity("NOTIFICATION_STATUS","state"));
            beanList.add(new ExcelExportEntity("REASONS_FOR_FAILURE","fReason"));
            beanList.add(new ExcelExportEntity("MAIL_CONTENT","mailContent"));
        }
        return beanList;
    }

    public static List<ExcelExportEntity> getEquipmentData(List<String> fieldList,boolean isCh){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if(isCh){
            beanList.add(new ExcelExportEntity("记录时间","inputdatetime"));
            for (String field : fieldList) {
                DataFieldEnum dataFieldEnum = DataFieldEnum.fromByLastDataField(field);
                if(null != dataFieldEnum){
                    String cName = dataFieldEnum.getCName();
                    String unit = dataFieldEnum.getUnit();
                    beanList.add(new ExcelExportEntity(cName+"("+unit+")",field));
                }
            }
        }else {
            beanList.add(new ExcelExportEntity("Time","inputdatetime"));
            for (String field : fieldList) {
                DataFieldEnum dataFieldEnum = DataFieldEnum.fromByLastDataField(field);
                if(null != dataFieldEnum){
                    String eName = dataFieldEnum.getEName();
                    String unit = dataFieldEnum.getUnit();
                    beanList.add(new ExcelExportEntity(eName+"("+unit+")",field));
                }
            }
        }

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
        }else {
            beanList.add(new ExcelExportEntity("TIME","date"));
        }
        dateList.forEach(res->{
            String hHmm = DateUtils.dateReduceHHmm(res);
            beanList.add(new ExcelExportEntity(hHmm,hHmm));
        });
        return beanList;
    }

    public static List<ExcelExportEntity> getAlarmData(boolean isCh) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if(isCh){
            beanList.add(new ExcelExportEntity("设备类型","equipmentTypeName"));
            beanList.add(new ExcelExportEntity("设备名称","equipmentName"));
            beanList.add(new ExcelExportEntity("设备报警次数","num"));
        }else {
            beanList.add(new ExcelExportEntity("EQUIPMENT_TYPE","equipmentTypeName"));
            beanList.add(new ExcelExportEntity("EQUIPMENT_NAME","equipmentName"));
            beanList.add(new ExcelExportEntity("NUMBER_OF_ALARMS","num"));
        }

        return beanList;
    }

    public static List<ExcelExportEntity> getOperationLog(boolean isCh) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if(isCh){
            beanList.add(new ExcelExportEntity("操作记录时间","operationtime"));
            beanList.add(new ExcelExportEntity("用户","username"));
            beanList.add(new ExcelExportEntity("操作类型","opeartiontype"));
            beanList.add(new ExcelExportEntity("菜单","functionname"));
            beanList.add(new ExcelExportEntity("医院","hospitalname"));
            beanList.add(new ExcelExportEntity("设备名称","equipmentname"));
        }else {
            beanList.add(new ExcelExportEntity("TIME","operationtime"));
            beanList.add(new ExcelExportEntity("USERNAME","username"));
            beanList.add(new ExcelExportEntity("OPERATION_TYPE","opeartiontype"));
            beanList.add(new ExcelExportEntity("MENU","functionname"));
            beanList.add(new ExcelExportEntity("HOSPITAL","hospitalname"));
            beanList.add(new ExcelExportEntity("EQ_NAME","equipmentname"));
        }

        return beanList;
    }

    /**
     * 获取MT310DC的excel标头
     * @return
     */
    public static List<ExcelExportEntity> getMT310DCEqData(boolean isCh) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if(isCh){
            beanList.add(new ExcelExportEntity("记录时间","inputdatetime"));
            beanList.add(new ExcelExportEntity("O2","currento2"));
            beanList.add(new ExcelExportEntity("CO2","currentcarbondioxide"));
            beanList.add(new ExcelExportEntity("VOC","currentvoc"));
            beanList.add(new ExcelExportEntity("外置探头温度","currenttemperature"));
            beanList.add(new ExcelExportEntity("外置探头湿度","currenthumidity"));
            beanList.add(new ExcelExportEntity("外置探头O2","outerO2"));
            beanList.add(new ExcelExportEntity("外置探头CO2","outerCO2"));
        }else {
            beanList.add(new ExcelExportEntity("TIME","inputdatetime"));
            beanList.add(new ExcelExportEntity("O2","currento2"));
            beanList.add(new ExcelExportEntity("CO2","currentcarbondioxide"));
            beanList.add(new ExcelExportEntity("VOC","currentvoc"));
            beanList.add(new ExcelExportEntity("EXTERNAL_PROBE_TEMP","currenttemperature"));
            beanList.add(new ExcelExportEntity("EXTERNAL_PROBE_RH","currenthumidity"));
            beanList.add(new ExcelExportEntity("EXTERNAL_PROBE_CO2","outerO2"));
            beanList.add(new ExcelExportEntity("EXTERNAL_PROBE_O2","outerCO2"));
        }
        return beanList;
    }

    public static List<ExcelExportEntity> getMT310DCEqData(boolean isCh,List<String> fields) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        beanList.add(isCh ? new ExcelExportEntity("记录时间","inputdatetime") : new ExcelExportEntity("TIME","inputdatetime"));
        for (String field : fields) {
            switch (field){
                case "currento2":
                    beanList.add(new ExcelExportEntity("O2","currento2"));
                    break;
                case "currentcarbondioxide":
                    beanList.add(new ExcelExportEntity("CO2","currentcarbondioxide"));
                    break;
                case "currentvoc":
                    beanList.add(new ExcelExportEntity("VOC","currentvoc"));
                    break;
                case "currenttemperature":
                    beanList.add(isCh ? new ExcelExportEntity("外置探头温度","currenttemperature"):new ExcelExportEntity("EXTERNAL_PROBE_TEMP","currenttemperature"));
                    break;
                case "currenthumidity":
                    beanList.add(isCh ? new ExcelExportEntity("外置探头湿度","currenthumidity") : new ExcelExportEntity("EXTERNAL_PROBE_RH","currenthumidity"));
                    break;
                case "outerO2":
                    beanList.add(isCh ? new ExcelExportEntity("外置探头O2","outerO2") :new ExcelExportEntity("EXTERNAL_PROBE_CO2","outerO2"));
                    break;
                case "outerCO2":
                    beanList.add(isCh ? new ExcelExportEntity("外置探头CO2","outerCO2") : new ExcelExportEntity("EXTERNAL_PROBE_O2","outerCO2"));
                    break;
            }
        }
        return beanList;
    }

}
