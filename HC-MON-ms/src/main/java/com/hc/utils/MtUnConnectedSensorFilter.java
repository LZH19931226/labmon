package com.hc.utils;

import com.hc.entity.Monitorequipmentlastdata;
import org.apache.commons.lang3.StringUtils;

/**
 * @author LiuZhiHao
 * @date 2020/7/19 11:41
 * 描述:
 **/
public class MtUnConnectedSensorFilter {


    public static String mtCheck(String sn) {
        switch (sn) {
            case "01":
                return "D-CK100";
            case "02":
                return "MT200L";
            case "03":
                return "D-CK900";
            case "04":
                return "MT100";
            case "05":
                return "MT200";
            case "06":
                return "MT300";
            case "07":
                return "MT300DC";
            case "08":
                return "MT300MIX";
            case "09":
                return "C400";
            case "10":
                return "MT500";
            case "11":
                return "MT600";
            case "12":
                return "MT400";
            case "13":
                return "MT100F";
            case "14":
                return "MT300LITE";
            case "15":
                return "MT700";
            case "16":
                return "MT300S";
            case "17":
                return "MT200M";
                //"MT200LM"
            case "18":
                return "MT200LM";
            case "19":
                return "MT200PLUS";
            case "20":
                return "MC20";
            case "21":
                return "MT300DCLite";
            case "22":
                return "NTECT-2000-1";
            case "23":
                return "NTECT-2000-4";
            case "24":
                return "MP2140-RFID";
            case "25":
                return "TLS-201";
            case "26":
                return "AMA100";
            case "27":
                return "AM100";
            case "28":
                return "MA100";
            case "29":
                return "AI100";
            case "30":
                return "MT300X";
            case "31":
                return "MT300X-S";
            case "32":
                return "MT1100";
            case "33":
                return "MT300SP";
            case "98":
                return "MTHX";
            case "97":
                return "MTHX";
            case "96":
                return "MTHX";
            default:
                return null;

        }
    }

    public static Monitorequipmentlastdata mtCheckUnCs(String sn, Monitorequipmentlastdata monitorequipmentlastdata) {
        String snType = sn.substring(4, 6);
        switch (snType) {
            case "18":
                //室温
                String currenttemperature2 = monitorequipmentlastdata.getCurrenttemperature2();
                //壁温
                String currenttemperature3 = monitorequipmentlastdata.getCurrenttemperature3();
                if (StringUtils.equals(currenttemperature2, "B")) {
                    monitorequipmentlastdata.setCurrenttemperature2(null);
                }
                if (StringUtils.equals(currenttemperature3, "B")) {
                    monitorequipmentlastdata.setCurrenttemperature3(null);
                }
                break;
            default:
                return monitorequipmentlastdata;

        }
        return monitorequipmentlastdata;
    }
}
