package com.hc.my.common.core.util;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.hc.my.common.core.constant.enums.DataFieldEnum;

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

    public static List<ExcelExportEntity> getEquipmentData(List<String> fieldList){
        List<ExcelExportEntity> beanList = new ArrayList<>();
        beanList.add(new ExcelExportEntity("记录时间","inputdatetime"));
        for (String field : fieldList) {
            DataFieldEnum dataFieldEnum = DataFieldEnum.fromByLastDataField(field);
            if(null != dataFieldEnum){
                String cName = dataFieldEnum.getCName();
                String unit = dataFieldEnum.getUnit();
                beanList.add(new ExcelExportEntity(cName+"("+unit+")",field));
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

    public static List<ExcelExportEntity> getDatePoint1(List<String> dateList,boolean isCh) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        if(isCh){
            beanList.add(new ExcelExportEntity("查询日期","date"));
            dateList.forEach(res->{
                beanList.add(new ExcelExportEntity(res,res));
            });
        }else {
            beanList.add(new ExcelExportEntity("query date","date"));
            dateList.forEach(res->{
                beanList.add(new ExcelExportEntity(res,res));
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
