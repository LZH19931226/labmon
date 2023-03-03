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

    /** 设备MT310DC 的设备监测类型编号*/
    public static final String EQ_MT310DC = "112";

    /** MT310DC 温度的EName */
    public static final String MT310DC_TEMP = "1";
    /** MT310DC 湿度 的EName */
    public static final String MT310DC_RH = "2";
    /** MT310DC 氧气的EName */
    public static final String MT310DC_O2 = "3";
    /** MT310DC 二氧化碳的EName */
    public static final String MT310DC_CO2 = "4";


    /** 通道2 */
    public static final String CHANNEL_2 = "2";

    /** 设备超时变红默认时间 */
    public static final String TIMEOUT_RED_DURATION = "30";
}
