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


}
