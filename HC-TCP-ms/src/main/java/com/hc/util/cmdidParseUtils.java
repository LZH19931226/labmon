package com.hc.util;

import com.hc.constant.DataRules;
import com.hc.my.common.core.bean.ParamaterModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static com.hc.util.paramaterModelUtils.*;
import static com.hc.util.paramaterModelUtils.electricity;

public class cmdidParseUtils {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(cmdidParseUtils.class);

    public static String paseAir(String co2) {

        if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSA)) {
            return DataRules.STATEA;
        } else
            // 若超出量程范围(0%-20%)，该值为 0xFF00； 针对mt400得异常值
            if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSB)) {
                return DataRules.STATEB;
            } else
                // 若已接传感器且已校准，但值无效，该值为 0xFFF0;
                if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSC)) {
                    return DataRules.STATEC;
                } else
                    // 若已接传感器，但未校准，该值为 0xFFFF；
                    if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSD)) {
                        return DataRules.STATED;
                    } else {
                        return gas(co2);
                    }
    }


    public static String paseAirMT400(String co2) {

        if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSA)) {
            return DataRules.STATEA;
        } else
            // 若超出量程范围(0%-20%)，该值为 0xFF00； 针对mt400得异常值
            if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSB)||StringUtils.equalsIgnoreCase(co2, "028C")||StringUtils.equalsIgnoreCase(co2, "9C00")) {
                return DataRules.STATEB;
            } else
                // 若已接传感器且已校准，但值无效，该值为 0xFFF0;
                if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSC)) {
                    return DataRules.STATEC;
                } else
                    // 若已接传感器，但未校准，该值为 0xFFFF；
                    if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSD)) {
                        return DataRules.STATED;
                    } else {
                        return gas(co2);
                    }
    }





    public static String paseAir10(String co2) {

        if (StringUtils.equalsIgnoreCase(co2, DataRules.OUTLIERSC)) {
            return DataRules.STATEE;
        } else{
                        return gas10(co2);
                    }
    }

    public static String pasetemperature(String substring2) {

        // 未接入传感器
        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSE)) {
            return DataRules.STATEA;
        } else
            // 未校验
            if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSD)) {
                return DataRules.STATED;
            } else
                // 超出范围超出 测试范围(-200℃到-185℃)
                if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSB)) {
                    return DataRules.STATEC;
                } else
                    //表示已接传感器且已校准，但值无效;
                    if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
                        return DataRules.STATEB;
                    } else {
                        //温度低于负200度设置为值无效
                        return paramaterModelUtils.temperature(substring2);

                    }

    }

    public static String pasetemperature2(String substring2) {

        // 未接入传感器
        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            return DataRules.STATEE;
        } else {
            return paramaterModelUtils.temperature10(substring2);
        }

    }



    public static String pasetemperature3(String substring2) {

        // 未接入传感器
        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            return DataRules.STATEE;
        } else if (StringUtils.equalsIgnoreCase(substring2,"FFF1")){
            return DataRules.STATEC;
        }else {
            return paramaterModelUtils.temperature10(substring2);
        }

    }



    public static String pasetemperature1(String tem) {
        if (StringUtils.equalsIgnoreCase(tem, DataRules.OUTLIERSA)) {
            return DataRules.STATEA;
        } else
            // 若超出量程范围(0%-20%)，该值为 0xFF00；
            if (StringUtils.equalsIgnoreCase(tem, DataRules.OUTLIERSB)) {
                return DataRules.STATEB;
            } else
                // 若已接传感器且已校准，但值无效，该值为 0xFFF0;
                if (StringUtils.equalsIgnoreCase(tem, DataRules.OUTLIERSC)) {
                    return DataRules.STATEC;
                } else
                    // 若已接传感器，但未校准，该值为 0xFFFF；
                    if (StringUtils.equalsIgnoreCase(tem, DataRules.OUTLIERSD)) {
                        return DataRules.STATED;
                    } else
                        // 未接传感器，该值未 0x F000;
                        if (StringUtils.endsWithIgnoreCase(tem, DataRules.OUTLIERSA)) {
                            return DataRules.STATEA;
                        } else {
                            return temperature(tem);

                        }
    }


    public static ParamaterModel Paser85(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        // 一路温度
        String substring2 = cmd.substring(28, 32);
        String pasetemperature = pasetemperature(substring2);
        //验证数据
        pasetemperature = CustomUtils.tem85(pasetemperature, sn);

        paramaterModel.setTEMP(pasetemperature);

        // 二路温度
        String substring4 = cmd.substring(32, 36);
        String pasetemperature1 = pasetemperature(substring4);
        pasetemperature1 = CustomUtils.tem85(pasetemperature1, sn);
        paramaterModel.setTEMP2(pasetemperature1);
        // 电量
        String pow = cmd.substring(36, 38);
        String electricity = electricity(pow);
        pow = CustomUtils.agreementAll(electricity, "0", "100");
        paramaterModel.setQC(pow);
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }


    public static ParamaterModel Paser87(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        // 二氧化碳
        String co2 = cmd.substring(28, 32);
        String s = paseAir(co2);
        s = CustomUtils.agreementAll(s, "0", "10");
        paramaterModel.setCO2(s);
        // 氧气
        String o2 = cmd.substring(32, 36);
        // 若未接 O2 传感器，该值为 0xF000
        String s1 = paseAir(o2);
        s1 = CustomUtils.agreementAll(s1, "0", "30");
        paramaterModel.setO2(s1);
        paramaterModel.setCmdid(cmdid);
        paramaterModel.setSN(sn);
        return paramaterModel;
    }

    public static ParamaterModel pase90(String data) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String one = data.substring(28, 32);
        String two = data.substring(32, 36);
        String three = data.substring(36, 40);
        String qc = data.substring(40, 42);
        String s = pasetemperature1(one);
        paramaterModel.setTEMP(s);
        String s1 = pasetemperature1(two);
        paramaterModel.setTEMP2(s1);
        String s2 = pasetemperature1(three);
        paramaterModel.setTEMP3(s2);
        String electricity = electricity(qc);
        paramaterModel.setQC(electricity);
        return paramaterModel;
    }

    public static ParamaterModel pase91(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String tem = cmd.substring(28, 32);
        String s2 = pasetemperature1(tem);
        s2 = CustomUtils.agreementAll(s2, "-100", "50");
        paramaterModel.setTEMP(s2);
        String yangqi = cmd.substring(32, 36);

        String s = paseAir(yangqi);
        s = CustomUtils.agreementAll(s, "0", "30");
        paramaterModel.setO2(s);
        String eryanghuatan = cmd.substring(36, 40);
        String s1 = paseAir(eryanghuatan);
        s1 = CustomUtils.agreementAll(s1, "0", "10");
        paramaterModel.setCO2(s1);
        return paramaterModel;
    }

    public static ParamaterModel pase92(String cmd) {

        ParamaterModel paramaterModel = new ParamaterModel();
        String zuo = cmd.substring(28, 32);
        if (StringUtils.equalsIgnoreCase(zuo, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(zuo, DataRules.OUTLIERSF)) {
            paramaterModel.setTEMP(DataRules.STATEB);
        } else if (StringUtils.equalsIgnoreCase(zuo, DataRules.OUTLIERSG)) {
            paramaterModel.setTEMP(DataRules.STATEC);
        } else if (StringUtils.equalsIgnoreCase(zuo, DataRules.OUTLIERSH)) {
            paramaterModel.setTEMP(DataRules.STATED);
        } else if (StringUtils.equalsIgnoreCase(zuo, DataRules.OUTLIERSI)) {
            paramaterModel.setTEMP(DataRules.STATEA);
        } else {
            String temperature1 = temperature(zuo);
            temperature1 = CustomUtils.agreementAll(temperature1, "0", "50");
            paramaterModel.setTEMP(temperature1);
        }


        String you = cmd.substring(32, 36);
        if (StringUtils.equalsIgnoreCase(you, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP2(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(you, DataRules.OUTLIERSF)) {
            paramaterModel.setTEMP2(DataRules.STATEB);
        } else if (StringUtils.equalsIgnoreCase(you, DataRules.OUTLIERSG)) {
            paramaterModel.setTEMP2(DataRules.STATEC);
        } else if (StringUtils.equalsIgnoreCase(you, DataRules.OUTLIERSH)) {
            paramaterModel.setTEMP2(DataRules.STATED);
        } else if (StringUtils.equalsIgnoreCase(you, DataRules.OUTLIERSI)) {
            paramaterModel.setTEMP2(DataRules.STATEA);
        } else {
            String temperature1 = temperature(you);
            temperature1 = CustomUtils.agreementAll(temperature1, "0", "50");
            paramaterModel.setTEMP2(temperature1);
        }


        String qi = cmd.substring(36, 40);
        if (StringUtils.equalsIgnoreCase(qi, DataRules.OUTLIERSC)) {
            paramaterModel.setAirflow(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(qi, DataRules.OUTLIERSF)) {
            paramaterModel.setAirflow(DataRules.STATEB);
        } else if (StringUtils.equalsIgnoreCase(qi, DataRules.OUTLIERSG)) {
            paramaterModel.setAirflow(DataRules.STATEC);
        } else if (StringUtils.equalsIgnoreCase(qi, DataRules.OUTLIERSH)) {
            paramaterModel.setAirflow(DataRules.STATED);
        } else if (StringUtils.equalsIgnoreCase(qi, DataRules.OUTLIERSI)) {
            paramaterModel.setAirflow(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(qi, DataRules.OUTLIERSJ)) {
            paramaterModel.setAirflow(DataRules.STATEA);
        } else {
            String airflow = electricity(qi);
            int a = new Integer(airflow);
            //  Double f = (double) a / 100;
            String chu = chu(a, "100");
            chu = CustomUtils.agreementAll(chu, "0", "1000");
            paramaterModel.setAirflow(chu);
        }

        return paramaterModel;
    }

    public static ParamaterModel pase93(String cmd) {

        ParamaterModel paramaterModel = new ParamaterModel();

        String substring1 = cmd.substring(28, 32);
        String substring2 = cmd.substring(32, 36);
        String substring3 = cmd.substring(36, 40);
        String substring4 = cmd.substring(40, 44);

        if (StringUtils.equalsIgnoreCase(substring1, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP(DataRules.STATEE);
        } else {
            String temperature = temperature(substring1);
            temperature = CustomUtils.agreementAll(temperature, "0", "50");
            paramaterModel.setTEMP(temperature);
        }

        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            paramaterModel.setCO2(DataRules.STATEE);
        } else {
            String gas = gas(substring2);
            gas = CustomUtils.agreementAll(gas, "0", "20");
            paramaterModel.setCO2(gas);
        }

        if (StringUtils.equalsIgnoreCase(substring3, DataRules.OUTLIERSC)) {
            paramaterModel.setRH(DataRules.STATEE);
        } else {
            String rh = gas(substring3);
            rh = CustomUtils.agreementAll(rh, "0", "100");
            paramaterModel.setRH(rh);
        }

        if (StringUtils.equalsIgnoreCase(substring4, DataRules.OUTLIERSC)) {
            paramaterModel.setO2(DataRules.STATEE);
        } else {
            String o2 = gas(substring4);
            o2 = CustomUtils.agreementAll(o2, "0", "30");
            paramaterModel.setO2(o2);
        }
        return paramaterModel;
    }

    public static ParamaterModel pase94(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 32);
        String substring2 = cmd.substring(32, 36);
        String substring3 = cmd.substring(36, 40);
        String substring4 = cmd.substring(40, 44);
        String substring5 = cmd.substring(44, 48);
        log.info("94协议解析：3路温度：" + substring5);
        String substring6 = cmd.substring(48, 52);
        String substring7 = cmd.substring(52, 56);


        if (StringUtils.equalsIgnoreCase(substring1, DataRules.OUTLIERSC)) {
            paramaterModel.setCO2(DataRules.STATEE);
        } else {
            String co2 = gas(substring1);
            co2 = CustomUtils.agreementAll(co2, "0", "20");
            paramaterModel.setCO2(co2);
        }

        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            paramaterModel.setO2(DataRules.STATEE);
        } else {
            String o2 = gas(substring2);
            o2 = CustomUtils.agreementAll(o2, "0", "30");
            paramaterModel.setO2(o2);
        }


        if (StringUtils.equalsIgnoreCase(substring3, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP(DataRules.STATEE);
        } else {
            String temperature1 = temperature(substring3);
            temperature1 = CustomUtils.agreementAll(temperature1, "0", "50");

            paramaterModel.setTEMP(temperature1);
        }


        if (StringUtils.equalsIgnoreCase(substring4, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP2(DataRules.STATEE);
        } else {
            String temperature2 = temperature(substring4);
            temperature2 = CustomUtils.agreementAll(temperature2, "0", "50");
            paramaterModel.setTEMP2(temperature2);
        }

        if (StringUtils.equalsIgnoreCase(substring5, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP3(DataRules.STATEE);
        } else {
            String temperature3 = temperature(substring5);
            temperature3 = CustomUtils.agreementAll(temperature3, "0", "50");
            paramaterModel.setTEMP3(temperature3);
        }

        if (StringUtils.equalsIgnoreCase(substring6, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP4(DataRules.STATEE);
        } else {
            String temperature4 = temperature(substring6);
            temperature4 = CustomUtils.agreementAll(temperature4, "0", "50");
            paramaterModel.setTEMP4(temperature4);
        }

        if (StringUtils.equalsIgnoreCase(substring7, DataRules.OUTLIERSC)) {
            paramaterModel.setTEMP5(DataRules.STATEE);
        } else {
            String temperature5 = temperature(substring7);
            temperature5 = CustomUtils.agreementAll(temperature5, "0", "50");
            paramaterModel.setTEMP5(temperature5);
        }


        return paramaterModel;
    }

    public static ParamaterModel pase97(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String wendu = cmd.substring(28, 32);
        String shidu = cmd.substring(32, 36);
        String PM05 = cmd.substring(36, 40);
        String PM50 = cmd.substring(40, 44);
        String O2 = cmd.substring(44, 48);
        String CO2 = cmd.substring(48, 52);
        String jiaquan = cmd.substring(52, 56);
        String temperature = temperature(wendu);
        paramaterModel.setTEMP(temperature);
        String gas = gas(shidu);
        paramaterModel.setRH(gas);
        String s = paramaterModelUtils.electricity1(PM05);
        paramaterModel.setPM05(s);
        String s1 = paramaterModelUtils.electricity1(PM50);
        paramaterModel.setPM50(s1);
        String s3 = pasetemperature1(O2);
        paramaterModel.setO2(s3);
        String s4 = pasetemperature1(CO2);
        paramaterModel.setCO2(s4);
        String s2 = paramaterModelUtils.electricity2(jiaquan);
        paramaterModel.setOX(s2);
        return paramaterModel;
    }

    /**
     * 双路温度电量查询应答，命令 ID：0x9B  (cmdid:9b)（MT200M 项目）
     *
     * @param cmd
     * @return
     */
    public static ParamaterModel pase9b(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String temp1 = cmd.substring(28, 32);// 用于展示温度
        String pasetemperature = pasetemperature(temp1);

        pasetemperature = CustomUtils.tem85(pasetemperature, sn);//验证数据
        String temp2 = cmd.substring(32, 36);// 用于校验温度
        String pasetemperature2 = pasetemperature(temp2);
        pasetemperature2 = CustomUtils.tem85(pasetemperature2, sn);
        paramaterModel.setTEMP(pasetemperature);
        paramaterModel.setTEMP2(pasetemperature2);
        String pow = cmd.substring(36, 38);
        String electricity = electricity(pow);
        pow = CustomUtils.agreementAll(electricity, "0", "100");
        paramaterModel.setQC(pow);
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }

    /**
     * 四路温度电量查询应答，命令 ID：0x9C（MT200LM 项目
     * 包含温度，校验温度 ，室温，壁温
     *
     * @param cmd
     * @return
     */
    public static ParamaterModel pase9c(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String temp1 = cmd.substring(28, 32);// 用于展示温度
        String pasetemperature = pasetemperature(temp1);

        pasetemperature = CustomUtils.tem85(pasetemperature, sn);//验证数据
        String temp2 = cmd.substring(32, 36);// 用于校验温度
        String pasetemperature2 = pasetemperature(temp2);
        pasetemperature2 = CustomUtils.tem85(pasetemperature2, sn);
        paramaterModel.setTEMP(pasetemperature);
        paramaterModel.setTEMP4(pasetemperature2);
        String temp3 = cmd.substring(36, 40);//室温
        String pasetemperature3 = pasetemperature(temp3);
        pasetemperature3 = CustomUtils.tem85(pasetemperature3, sn);
        paramaterModel.setTEMP2(pasetemperature3);
        String temp4 = cmd.substring(40, 44);//壁温
        String pasetemperature4 = pasetemperature(temp4);
        pasetemperature4 = CustomUtils.tem85(pasetemperature4, sn);
        String substring = cmd.substring(44, 46);//电量
        int parseInt1 = Integer.parseInt(substring, 16);
        String s = String.valueOf(parseInt1);
        paramaterModel.setTEMP3(pasetemperature4);
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        paramaterModel.setQC(s);
        return paramaterModel;

    }

    /**
     * 终端上传 BT37 培养箱获取的数据，命令 ID：0x9F（MT700 项目) 奥利金培养箱数据
     *
     * @param cmd
     * @param sn
     * @param cmdid
     * @return
     */
    public static ParamaterModel pase9f(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 32);
        if (!StringUtils.equalsIgnoreCase(substring1, DataRules.OUTLIERSC)) {
            String temp1 = temperature(substring1);//左舱室顶部温度
            // temp1 = CustomUtils.tem85(temp1, sn);//验证数据
            paramaterModel.setTEMP(temp1);
        } else {
            paramaterModel.setTEMP(DataRules.STATEA);//值无效
        }
        String substring2 = cmd.substring(32, 36);
        if (!StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            String temp2 = temperature(substring2);//右舱室顶部温度
            //  temp2 = CustomUtils.tem85(temp2, sn);//验证数据
            paramaterModel.setTEMP2(temp2);
        } else {
            paramaterModel.setTEMP2(DataRules.STATEA);
        }
        String substring3 = cmd.substring(36, 40);
        if (!StringUtils.equalsIgnoreCase(substring3, DataRules.OUTLIERSC)) {
            String temp3 = temperature(substring3);//左舱室底部温度
            //    temp3 = CustomUtils.tem85(temp3, sn);//验证数据
            paramaterModel.setTEMP3(temp3);
        } else {
            paramaterModel.setTEMP3(DataRules.STATEA);
        }
        String substring4 = cmd.substring(40, 44);
        if (!StringUtils.equalsIgnoreCase(substring4, DataRules.OUTLIERSC)) {
            String temp4 = temperature(cmd.substring(40, 44));//右舱室底部温度
            //   temp4 = CustomUtils.tem85(temp4, sn);//验证数据
            paramaterModel.setTEMP4(temp4);
        } else {
            paramaterModel.setTEMP4(DataRules.STATEA);
        }
        String substring5 = cmd.substring(44, 48);
        if (!StringUtils.equalsIgnoreCase(substring5, DataRules.OUTLIERSC)) {
            String temp5 = temperature(cmd.substring(44, 48));//加湿器温度
            //    temp5 = CustomUtils.tem85(temp5, sn);//验证数据
            paramaterModel.setTEMP5(temp5);
        } else {
            paramaterModel.setTEMP5(DataRules.STATEA);
        }
        //气体流速
        String substring = cmd.substring(48, 52);
        if (!StringUtils.equalsIgnoreCase(substring, DataRules.OUTLIERSC)) {
            String airflow = electricity(substring);
            Integer integer = new Integer(airflow);
            String chu = chu(integer, "10");
            paramaterModel.setTEMP6(chu);
        } else {
            paramaterModel.setTEMP6(DataRules.STATEA);
        }
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;

    }

    public static ParamaterModel paseA1(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 32);//舱室一温度
        if (StringUtils.equalsIgnoreCase(substring1, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP(DataRules.STATEE);
        } else {
            String temperature = temperature(substring1);
            paramaterModel.setTEMP(temperature);
        }
        String substring2 = cmd.substring(32, 36);//舱室二温度
        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP2(DataRules.STATEE);
        } else {
            String temperature = temperature(substring2);
            paramaterModel.setTEMP2(temperature);
        }
        String substring3 = cmd.substring(36, 40);//舱室三温度
        if (StringUtils.equalsIgnoreCase(substring3, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP3(DataRules.STATEE);
        } else {
            String temperature = temperature(substring3);
            paramaterModel.setTEMP3(temperature);
        }
        String substring4 = cmd.substring(40, 44);//舱室四温度
        if (StringUtils.equalsIgnoreCase(substring4, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP4(DataRules.STATEE);
        } else {
            String temperature = temperature(substring4);
            paramaterModel.setTEMP4(temperature);
        }
        String substring5 = cmd.substring(44, 48);//舱室五温度
        if (StringUtils.equalsIgnoreCase(substring5, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP5(DataRules.STATEE);
        } else {
            String temperature = temperature(substring5);
            paramaterModel.setTEMP5(temperature);
        }
        String substring6 = cmd.substring(48, 52);//舱室六温度
        if (StringUtils.equalsIgnoreCase(substring6, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP6(DataRules.STATEE);
        } else {
            String temperature = temperature(substring6);
            paramaterModel.setTEMP6(temperature);
        }
        String substring7 = cmd.substring(52, 56);//舱室七温度
        if (StringUtils.equalsIgnoreCase(substring7, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP7(DataRules.STATEE);
        } else {
            String temperature = temperature(substring7);
            paramaterModel.setTEMP7(temperature);
        }
        String substring8 = cmd.substring(56, 60);//舱室八温度
        if (StringUtils.equalsIgnoreCase(substring8, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP8(DataRules.STATEE);
        } else {
            String temperature = temperature(substring8);
            paramaterModel.setTEMP8(temperature);
        }
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }

    //4843 A2 1A 31383335313530303031 0E74 0E74 01F4 0258 0064 0064 0032 0000 24 23
    public static ParamaterModel paseA2(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 32);//舱室九温度
        if (StringUtils.equalsIgnoreCase(substring1, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP9(DataRules.STATEE);
        } else {
            String temperature = temperature(substring1);
            paramaterModel.setTEMP9(temperature);
        }
        String substring2 = cmd.substring(32, 36);//舱室十温度
        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setTEMP10(DataRules.STATEE);
        } else {
            String temperature = temperature(substring2);
            paramaterModel.setTEMP10(temperature);
        }
        String substring3 = cmd.substring(36, 40);//O2浓度
        if (StringUtils.equalsIgnoreCase(substring3,DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setO2(DataRules.STATEE);
        } else {
            String o2 = gas(substring3);
            paramaterModel.setO2(o2);
        }

        String substring4 = cmd.substring(40, 44);//CO2浓度
        if (StringUtils.equalsIgnoreCase(substring4,DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setCO2(DataRules.STATEE);
        } else {
            String co2 = gas(substring4);
            paramaterModel.setCO2(co2);
        }
        String substring5 = cmd.substring(44, 48);//N2
        if (StringUtils.equalsIgnoreCase(substring5,DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setN2(DataRules.STATEC);
        } else {
            String n2 = electricity2(substring5);
            paramaterModel.setN2(n2);
        }
        String substring6 = cmd.substring(48, 52);//CO2压力
        if (StringUtils.equalsIgnoreCase(substring6,DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setPRESS(DataRules.STATEC);
        } else {
            String press = electricity2(substring6);
            paramaterModel.setPRESS(press);
        }
        String substring7 = cmd.substring(52, 56);//气流
        if (StringUtils.equalsIgnoreCase(substring7,DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setAirflow(DataRules.STATEA);
        } else {
            String airFolw = electricity(substring7);
            //扩大1000倍，转化为ML
            paramaterModel.setAirflow(airFolw);
        }
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }


    /**
     * 桌面培养箱DC
     *
     * @param cmd
     * @return
     */

    //48438A1731383532303131303031 04 01F4 01F1 03E8 0000 0047 0047 73 23
    public static ParamaterModel pase8a(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String type = cmd.substring(28, 30);
        switch (type) {
            case "03":
                String temp1 = cmd.substring(30, 34);
                String temp2 = cmd.substring(34, 38);
                String temp3 = cmd.substring(38, 42);
                String temp4 = cmd.substring(42, 46);
                String temp5 = cmd.substring(46, 50);
                String temp6 = cmd.substring(50, 54);
                String temp7 = cmd.substring(54, 58);
                String temp8 = cmd.substring(58, 62);
                String temp9 = cmd.substring(62, 66);
                String temp10 = cmd.substring(66, 70);
                String temperature1 = paramaterModelUtils.temperature(temp1);
                String temperature2 = paramaterModelUtils.temperature(temp2);
                String temperature3 = paramaterModelUtils.temperature(temp3);
                String temperature4 = paramaterModelUtils.temperature(temp4);
                String temperature5 = paramaterModelUtils.temperature(temp5);
                String temperature6 = paramaterModelUtils.temperature(temp6);
                String temperature7 = paramaterModelUtils.temperature(temp7);
                String temperature8 = paramaterModelUtils.temperature(temp8);
                String temperature9 = paramaterModelUtils.temperature(temp9);
                String temperature10 = paramaterModelUtils.temperature(temp10);
                paramaterModel.setTEMP(temperature1);
                paramaterModel.setTEMP2(temperature2);
                paramaterModel.setTEMP3(temperature3);
                paramaterModel.setTEMP4(temperature4);
                paramaterModel.setTEMP5(temperature5);
                paramaterModel.setTEMP6(temperature6);
                paramaterModel.setTEMP7(temperature7);
                paramaterModel.setTEMP8(temperature8);
                paramaterModel.setTEMP9(temperature9);
                paramaterModel.setTEMP10(temperature10);
                break;
            case "04":
                String o2 = cmd.substring(30, 34);
                String co2 = cmd.substring(34, 38);
                String traffic1 = cmd.substring(38, 42);
                String traffic2 = cmd.substring(42, 46);
                String pressure1 = cmd.substring(46, 50);
                String pressure2 = cmd.substring(50, 54);
                String gas = gas(o2);
                String gas1 = gas(co2);
                String gas3 = gas(traffic1);
                String gas4 = gas(traffic2);
                String gas5 = gas(pressure1);
                String gas6 = gas(pressure2);
                //氧气
                paramaterModel.setO2(gas);
                //二氧化碳
                paramaterModel.setCO2(gas1);
                //流量1
                paramaterModel.setPM05(gas3);
                //流量2
                paramaterModel.setPM50(gas4);
                //气压1
                paramaterModel.setRH(gas5);
                //气压2
                paramaterModel.setOX(gas6);
                break;
        }


        return paramaterModel;
    }

    public static String chu(Integer a, String b) {
        return new BigDecimal(a).divide(new BigDecimal(b), 2, BigDecimal.ROUND_HALF_UP).toString();


    }

    public static ParamaterModel pase8b(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String type = cmd.substring(28, 30);
        switch (type) {
            case "02":
                //温度探头组数 单位组
                String substring = cmd.substring(31, 32);
                paramaterModel.setTEMP(substring);
                //温度探头编号
                String substring2 = cmd.substring(32, 34);
                if (StringUtils.equals("00", substring2)) {
                    paramaterModel.setTEMP2("一号");
                }
                if (StringUtils.equals("01", substring2)) {
                    paramaterModel.setTEMP2("二号");
                }
                //偏移值
                String gas = gas(cmd.substring(34, 38));
                paramaterModel.setTEMP3(gas);
                break;
            case "03":
                String substring1 = cmd.substring(30, 34);
                String gas1 = gas(substring1);
                paramaterModel.setO2(gas1);
                break;
            case "04":
                String substring3 = cmd.substring(30, 34);
                String gas2 = gas(substring3);
                paramaterModel.setCO2(gas2);
                break;
            case "05":
                //流量传感器编号
                String substring4 = cmd.substring(31, 32);
                paramaterModel.setTEMP(substring4);
                String gas3 = gas(substring4);
                paramaterModel.setAirflow(gas3);
                break;
            case "06":
                //压力传感器编号
                String substring5 = cmd.substring(31, 32);
                paramaterModel.setTEMP(substring5);
                String gas4 = gas(substring5);
                paramaterModel.setPRESS(gas4);
                break;
        }

        return paramaterModel;
    }

    public static ParamaterModel pase8c(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String type = cmd.substring(30, 32);
        switch (type) {
            case "01":
                //仓盖打开超时报警
                paramaterModel.setTEMP("仓盖打开超时报警");
                //00 表示正常， 01 表示报警
                String substring = cmd.substring(32, 34);
                if (StringUtils.equals(substring, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring1 = cmd.substring(35, 36);
                paramaterModel.setTEMP3(substring1);
                break;
            case "02":
                //温度大于高阈值报警
                paramaterModel.setTEMP("温度大于高阈值报警");
                //00 表示正常， 01 表示报警
                String substring2 = cmd.substring(34, 36);
                if (StringUtils.equals(substring2, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring3 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring3);
                break;
            case "03":
                //温度小于低阈值报警
                paramaterModel.setTEMP("温度小于低阈值报警");
                //00 表示正常， 01 表示报警
                String substring4 = cmd.substring(34, 36);
                if (StringUtils.equals(substring4, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring5 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring5);
                break;
            case "04":
                //O2 大于高阈值报警
                paramaterModel.setTEMP("O2 大于高阈值报警");
                //00 表示正常， 01 表示报警
                String substring6 = cmd.substring(34, 36);
                if (StringUtils.equals(substring6, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring7 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring7);
                break;
            case "05":
                //O2 小于低阈值报警
                paramaterModel.setTEMP("O2 小于低阈值报警");
                //00 表示正常， 01 表示报警
                String substring8 = cmd.substring(34, 36);
                if (StringUtils.equals(substring8, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring9 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring9);
                break;
            case "06":
                //CO2 高于高阈值报警
                paramaterModel.setTEMP("CO2 高于高阈值报警");
                //00 表示正常， 01 表示报警
                String substring10 = cmd.substring(34, 36);
                if (StringUtils.equals(substring10, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring11 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring11);
                break;
            case "07":
                //CO2 低于低阈值报警
                paramaterModel.setTEMP("CO2 低于低阈值报警");
                //00 表示正常， 01 表示报警
                String substring12 = cmd.substring(34, 36);
                if (StringUtils.equals(substring12, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring13 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring13);
                break;
            case "08":
                //流量高于高阈值报警
                paramaterModel.setTEMP("流量高于高阈值报警");
                //00 表示正常， 01 表示报警
                String substring14 = cmd.substring(34, 36);
                if (StringUtils.equals(substring14, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring15 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring15);
                break;
            case "09":
                //流量低于低阈值报警
                paramaterModel.setTEMP("流量低于低阈值报警");
                //00 表示正常， 01 表示报警
                String substring16 = cmd.substring(34, 36);
                if (StringUtils.equals(substring16, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring17 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring17);
                break;
            case "0a":
                //压力高于高阈值报警
                paramaterModel.setTEMP("压力高于高阈值报警");
                //00 表示正常， 01 表示报警
                String substring18 = cmd.substring(34, 36);
                if (StringUtils.equals(substring18, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring19 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring19);
                break;
            case "0b":
                //压力低于低阈值报警
                paramaterModel.setTEMP("压力低于低阈值报警");
                //00 表示正常， 01 表示报警
                String substring20 = cmd.substring(34, 36);
                if (StringUtils.equals(substring20, "00")) {
                    paramaterModel.setTEMP2("正常");
                } else {
                    paramaterModel.setTEMP2("报警");
                }
                //仓室号
                String substring21 = cmd.substring(37, 38);
                paramaterModel.setTEMP3(substring21);
                break;
        }


        return paramaterModel;
    }

    //48 43 98 0B 31 37 35 33 30 38 30 30 30 36 03 95 23
    public static ParamaterModel pase98(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String type = cmd.substring(28, 30);
        String electricity = paramaterModelUtils.electricity(type);
        switch (electricity) {
            //正常->预警； (报警信息：有漏气倾向)
            case "1":
                paramaterModel.setAirflow("G");
                break;
            //预警->正常；(报警信息：恢复正常)
            case "2":
                paramaterModel.setAirflow("H");
                break;
            //预警->报警； (报警信息：发生报警事件)
            case "3":
                paramaterModel.setAirflow("I");
                break;
            //报警->关闭； (报警信息： 手动关闭报警)
            case "4":
                paramaterModel.setAirflow("J");
                break;
            //设备正常运行； (状态信息， 3 分钟/次)
            case "5":
                paramaterModel.setAirflow("K");
                break;
            //设备漏气预警； (状态信息， 3 分钟/次)
            case "6":
                paramaterModel.setAirflow("L");
                break;
            //设备漏气报警； (状态信息， 3 分钟/次)
            case "7":
                paramaterModel.setAirflow("M");
                break;
            //手动关闭报警； (状态信息， 3 分钟/次)
            case "8":
                paramaterModel.setAirflow("N");
                break;
            //设备气压低报警；(状态信息，3 分钟/次)
            case "9":
                paramaterModel.setAirflow("O");
                break;
            //手动关闭气压低报警；(状态信息，3 分钟/次)
            case "10":
                paramaterModel.setAirflow("P");
                break;
            //休眠模式；(状态信息，3 分钟/次)
            case "11":
                paramaterModel.setAirflow("Q");
                break;
            default:
                return null;
        }

        return paramaterModel;
    }

    public static ParamaterModel pase99(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String temperature1 = temperature(cmd.substring(28, 32));
        paramaterModel.setTEMP(temperature1);
        String temperature2 = temperature(cmd.substring(32, 36));
        paramaterModel.setTEMP2(temperature2);
        String temperature3 = temperature(cmd.substring(36, 40));
        paramaterModel.setTEMP3(temperature3);
        String temperature4 = temperature(cmd.substring(40, 44));
        paramaterModel.setTEMP4(temperature4);
        String temperature5 = temperature(cmd.substring(44, 48));
        paramaterModel.setTEMP5(temperature5);
        String temperature6 = temperature(cmd.substring(48, 52));
        paramaterModel.setTEMP6(temperature6);
        String temperature7 = temperature(cmd.substring(52, 56));
        paramaterModel.setTEMP7(temperature7);
        String temperature8 = temperature(cmd.substring(56, 60));
        paramaterModel.setTEMP8(temperature8);
        String temperature9 = temperature(cmd.substring(60, 64));
        paramaterModel.setTEMP9(temperature9);
        String temperature10 = temperature(cmd.substring(64, 68));
        paramaterModel.setTEMP10(temperature10);
        String gas = gas(cmd.substring(68, 72));
        paramaterModel.setO2(gas);
        String gas1 = gas(cmd.substring(72, 76));
        paramaterModel.setCO2(gas1);
        return paramaterModel;
    }

    public static ParamaterModel pase9A(String cmd) {
        ParamaterModel paramaterModel = new ParamaterModel();
        // 左盖板温度
        String substring1 = cmd.substring(28, 32);
        if (!StringUtils.equalsIgnoreCase("FFF0", substring1)) {
            String temperature1 = temperature(cmd.substring(28, 32));
            paramaterModel.setTEMP(temperature1);
        }
        //左底板温度
        String substring2 = cmd.substring(32, 36);
        if (!StringUtils.equalsIgnoreCase("FFF0", substring2)) {
            String temperature2 = temperature(substring2);
            paramaterModel.setTEMP2(temperature2);
        }
        //左气流速度
        String substring = cmd.substring(36, 40);
        if (!StringUtils.equalsIgnoreCase(substring, "FFF0")) {
            String airflow = electricity(substring);
            Integer integer = new Integer(airflow);
            String chu = chu(integer, "10");
            paramaterModel.setTEMP3(chu);
        }
        //右盖板温度
        String substring3 = cmd.substring(40, 44);
        if (!StringUtils.equalsIgnoreCase("FFF0", substring3)) {
            String temperature = temperature(substring3);
            paramaterModel.setTEMP4(temperature);
        }
        String substring4 = cmd.substring(44, 48);
        if (!StringUtils.equalsIgnoreCase("FFF0", substring4)) {
            String temperature = temperature(substring4);
            paramaterModel.setTEMP5(temperature);
        }
        String substring5 = cmd.substring(48, 52);
        if (!StringUtils.equalsIgnoreCase("FFF0", substring5)) {
            String temperature = temperature(substring5);
            paramaterModel.setTEMP6(temperature);
        }
        return paramaterModel;

    }


    public static void main(String[] args) {
        String id = "4843980b31383838303830303031099123";
        String s = id.replaceAll(" ", "");
//        String s="48439110313830383939303032380E7300B102276823";
        ParamaterModel paramaterModel = pase98(s);
        System.out.println(paramaterModel);
    }

    //4843 A3 19 31383335313530303031 0E74 07D0 0063 0E73 07CF 0062 0100 00 A3 23
    public static ParamaterModel paseA3(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        //左舱室温度
        String substring1 = cmd.substring(28, 32);
        if (StringUtils.equalsIgnoreCase(substring1, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据(设备异常或培养箱断电);
            paramaterModel.setLeftCompartmentTemp(DataRules.STATEE);
        } else {
            String temperature = temperature(substring1);
            paramaterModel.setLeftCompartmentTemp(temperature);
        }
        //左舱室流量
        String substring2 = cmd.substring(32, 36);
        if (StringUtils.equalsIgnoreCase(substring2, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setLeftCompartmentFlow(DataRules.STATEE);
        } else {
            String temperature = gas(substring2);
            paramaterModel.setLeftCompartmentFlow(temperature);
        }
        //左舱室湿度
        String substring3 = cmd.substring(36, 40);
        if (StringUtils.equalsIgnoreCase(substring3, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setLeftCompartmentHumidity(DataRules.STATEE);
        } else {
            String temperature = electricity(substring3);
            paramaterModel.setLeftCompartmentHumidity(temperature);
        }
        //右舱室温度
        String substring4 = cmd.substring(40, 44);
        if (StringUtils.equalsIgnoreCase(substring4, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据(设备异常或培养箱断电);
            paramaterModel.setRightCompartmentTemp(DataRules.STATEE);
        } else {
            String temperature = temperature(substring4);
            paramaterModel.setRightCompartmentTemp(temperature);
        }
        String substring5 = cmd.substring(44, 48);
        //右舱室流量
        if (StringUtils.equalsIgnoreCase(substring5, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setRightCompartmentFlow(DataRules.STATEE);
        } else {
            String temperature = gas(substring5);
            paramaterModel.setRightCompartmentFlow(temperature);
        }
        //右舱室湿度
        String substring6 = cmd.substring(48, 52);
        if (StringUtils.equalsIgnoreCase(substring6, DataRules.OUTLIERSC)) {
            // FFF0   == 未获取到数据
            paramaterModel.setRightCompartmentHumidity(DataRules.STATEE);
        } else {
            String temperature = electricity(substring6);
            paramaterModel.setRightCompartmentHumidity(temperature);
        }
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }

    public static ParamaterModel paseA4(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 30);
        String electricity = electricity(substring1);
        //0：适配器供电正常；1：适配器供电异常
        paramaterModel.setUPS(electricity);
        String substring2 = cmd.substring(30, 34);
        String electricity1 = electricity(substring2);
        paramaterModel.setVoltage(electricity1);
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }

    public static ParamaterModel paseA5(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 32);
        paramaterModel.setTEMP(pasetemperature1(substring1));
        String substring2 = cmd.substring(32, 36);
        paramaterModel.setO2(paseAir(substring2));
        String substring3 = cmd.substring(36, 40);
        paramaterModel.setCO2(paseAir(substring3));
        String substring4 = cmd.substring(40, 44);
        paramaterModel.setRH(paseAir(substring4));
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }

    public static ParamaterModel paseA6(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 32);
        paramaterModel.setTEMP(pasetemperature2(substring1));
        String substring2 = cmd.substring(32, 36);
        paramaterModel.setTEMP2(pasetemperature2(substring2));
        String substring3 = cmd.substring(36, 40);
        paramaterModel.setTEMP3(pasetemperature2(substring3));
        String substring4 = cmd.substring(40, 44);
        paramaterModel.setTEMP4(pasetemperature2(substring4));
        String substring5 = cmd.substring(44, 48);
        paramaterModel.setTEMP5(pasetemperature2(substring5));
        String substring6 = cmd.substring(48, 52);
        paramaterModel.setTEMP6(pasetemperature2(substring6));
        String substring7 = cmd.substring(52, 56);
        paramaterModel.setO2(paseAir10(substring7));
        String substring8 = cmd.substring(56, 60);
        paramaterModel.setCO2(paseAir10(substring8));
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }

    public static ParamaterModel paseA7(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;
    }

    public static ParamaterModel paseA8(String cmd, String sn, String cmdid) {
        ParamaterModel paramaterModel = new ParamaterModel();
        String substring1 = cmd.substring(28, 32);
        paramaterModel.setTEMP(pasetemperature3(substring1));
        String substring2 = cmd.substring(32, 34);
        if (StringUtils.equalsIgnoreCase(substring2,"F0")){
            paramaterModel.setRH(DataRules.STATEE);
        }else if (StringUtils.equalsIgnoreCase(substring2,"F1")){
            paramaterModel.setRH(DataRules.STATEC);
        }else {
            paramaterModel.setRH(electricity(substring2));
        }
        String substring3 = cmd.substring(34, 36);
        if (StringUtils.equalsIgnoreCase(substring3,"F0")){
            paramaterModel.setCO2(DataRules.STATEE);
        }else if (StringUtils.equalsIgnoreCase(substring3,"F1")){
            paramaterModel.setCO2(DataRules.STATEC);
        }else {
            paramaterModel.setCO2(gas(substring3));
        }
        String substring4 = cmd.substring(36, 40);
        if (StringUtils.equalsIgnoreCase(substring4, DataRules.OUTLIERSC)) {
            paramaterModel.setO2(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(substring4,"FFF1")){
            paramaterModel.setO2(DataRules.STATEC);
        }else if (StringUtils.equalsIgnoreCase(substring4,"FFF2")){
            paramaterModel.setO2(DataRules.STATED);
        }else {
            paramaterModel.setO2(gas10(substring4));
        }
        String substring5 = cmd.substring(40, 44);
        if (StringUtils.equalsIgnoreCase(substring5, DataRules.OUTLIERSC)) {
            paramaterModel.setPRESS(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(substring5,"FFF1")){
            paramaterModel.setPRESS(DataRules.STATEC);
        }else {
            paramaterModel.setPRESS(electricity(substring5));
        }
        String substring6 = cmd.substring(44, 48);
        if (StringUtils.equalsIgnoreCase(substring6, DataRules.OUTLIERSC)) {
            paramaterModel.setPM25(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(substring6,"FFF1")){
            paramaterModel.setPM25(DataRules.STATEC);
        }else {
            paramaterModel.setPM25(electricity(substring6));
        }
        String substring7 = cmd.substring(48, 52);
        if (StringUtils.equalsIgnoreCase(substring7, DataRules.OUTLIERSC)) {
            paramaterModel.setPM10(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(substring7,"FFF1")){
            paramaterModel.setPM10(DataRules.STATEC);
        }else {
            paramaterModel.setPM10(electricity(substring7));
        }
        String substring8 = cmd.substring(52, 56);
        if (StringUtils.equalsIgnoreCase(substring8, DataRules.OUTLIERSC)) {
            paramaterModel.setOX(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(substring8,"FFF1")){
            paramaterModel.setOX(DataRules.STATEC);
        }else {
            paramaterModel.setOX(electricity2(substring8));
        }
        String substring9 = cmd.substring(56, 60);
        if (StringUtils.equalsIgnoreCase(substring9, DataRules.OUTLIERSC)) {
            paramaterModel.setVOC(DataRules.STATEE);
        } else if (StringUtils.equalsIgnoreCase(substring9,"FFF1")){
            paramaterModel.setVOC(DataRules.STATEC);
        }else {
            paramaterModel.setVOC(electricity2(substring9));
        }
        paramaterModel.setSN(sn);
        paramaterModel.setCmdid(cmdid);
        return paramaterModel;

    }
}
