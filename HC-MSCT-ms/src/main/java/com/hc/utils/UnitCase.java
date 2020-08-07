package com.hc.utils;

/**
 * @author LiuZhiHao
 * @date 2020/8/7 15:34
 * 描述:
 **/
public class UnitCase {

    public static  String caseUint(String uint) {
        switch (uint) {
            case "I":
                return "发生漏气";
            case "M":
                return "设备漏气";
            case "O":
                return "设备气压低";
            default:
                return uint;

        }
    }
}
