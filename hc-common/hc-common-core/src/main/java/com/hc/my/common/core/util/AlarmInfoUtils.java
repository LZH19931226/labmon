package com.hc.my.common.core.util;

import com.hc.my.common.core.constant.enums.DataFieldEnum;

import javax.print.DocFlavor;

public class AlarmInfoUtils {
    //The temperature of the device name is abnormal data is
    private final static String reMarkFormat = "The %s of the %s is abnormal data is %s";

    //胚胎库:O2异常,异常数据为:11.11
    public static String setTypeName(String remark,String eName){
        return String.format(reMarkFormat,eName,getEquipmentName(remark),getAbnormalValue(remark));
    }

    /**
     * 获取设备名称 从开始到第一个：
     * @param remark
     * @return
     */
    public static String getEquipmentName(String remark){
        if(remark.contains(":")){
            int first = remark.indexOf(":");
            return remark.substring(0, first);
        }
        return "NON";
    }

    /**
     * 获取异常值
     * @param remark
     * @return
     */
    public static String getAbnormalValue(String remark){
        if(remark.contains(":")){
                int last = remark.lastIndexOf(":");
            String data = remark.substring(last+1);
            String specialValue = specialValueValidation(data);
            return specialValue;
        }
        return "NON";
    }

    private static String specialValueValidation(String data) {
        switch (data){
            case "市电异常":
                return "ups abnormality";
            case "常闭":
                return "normally closed";
            case "常开":
                return "normally open";
            case "未获取到数据":
                return "no data fetched";
            case "流量控制关闭":
                return "flow control is off";
            case "气体流量不稳定":
                return "the gas flow is unstable";
            case "气口压力低":
                return "low air port pressure";
            case "总开关关闭，但未断电":
                return "the main switch is off but not powered off";
            case "发生漏气报警事件":
                return "an air leak alarm event has occurred";
            case "设备漏气报警":
                return "equipment leakage alarm";
            case "设备气压低报警":
                return "low air pressure of the device alarm";
            case "报警信号异常":
                return "the alarm signal is abnormal";
            default:
                return data;
        }
    }
}
