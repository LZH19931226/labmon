package com.hc.my.common.core.constant.enums;

import lombok.Data;

@Data
public class SysConstants {
    //设备探头状态 0为正常 1为报警中
    public  static final  String NORMAL = "0";
    public static final  String IN_ALARM = "1";

    public static final  String PHONE_SMS_NOTIFICATIONS = "(电话通知/短信通知)";
    public static final  String SMS_NOTIFICATIONS = "(短信通知)";
    public static final  String PHONE_NOTIFICATIONS = "(电话通知)";

    /** 设备状态 */
    public  static final  String EQ_NORMAL = "0";
    public  static final  String EQ_ABNORMAL = "1";
    public  static final  String EQ_TIMEOUT = "2";

    /** 通过截取sn5、6位 等到设备*/
    public static final String MT310_SN = "31";
    public static final String INPUT_DATETIME = "inputdatetime";
    /** 设备MT310DC 的设备监测类型编号*/
    public static final String EQ_MT310DC = "112";

    /** MT310DC 缓存中的Eanme */
    public static final String MT310DC_TEMP = "1";
    public static final String MT310DC_RH = "2";
    public static final String MT310DC_O2 = "3";
    public static final String MT310DC_CO2 = "4";
    /** MT310 曲线字段 */
    public static final String MT310DC_DATA_CO2 = "currentcarbondioxide";
    public static final String MT310DC_DATA_O2 = "currento2";
    public static final String MT310DC_DATA_VOC = "currentvoc";
    public static final String MT310DC_DATA_TEMP = "currenttemperature";
    public static final String MT310DC_DATA_RH = "currenthumidity";
    public static final String MT310DC_DATA_OUTER_O2 = "outerO2";
    public static final String MT310DC_DATA_OUTER_CO2 = "outerCO2";

    /** 通道2 */
    public static final String CHANNEL_2 = "2";

    /** 设备超时变红默认时间 */
    public static final String TIMEOUT_RED_DURATION = "30";
}
