package com.hc.my.common.core.bean;


import lombok.Data;

import java.util.Date;

/**
 * Created by 16956 on 2018-07-31.
 */

@Data
public class ParamaterModel {
    //设备sn号"
    private String SN;
    //命令id"
    private String cmdid;
    //"CO2"
    private String CO2;
    //"O2"
    private String O2;
    //"空气质量"
    private String VOC;
    //一路温度"
    private String TEMP;
    //二路温度"
    private String TEMP2;
    //"三路温度"
    private String TEMP3;
    //"四路温度"
    private String TEMP4;
    //五路温度"
    private String TEMP5;
    //六路温度"
    private String TEMP6;
    //"七路温度"
    private String TEMP7;
    //"八路温度"
    private String TEMP8;
    //"九路温度"
    private String TEMP9;
    //"十路温度"
    private String TEMP10;
    //"气流"
    private String airflow;
    //"湿度"
    private String RH;
    //"压力"
    private String PRESS;
    //"电量"
    private String QC;
    //"PM2.5"
    private String PM25;
    //"PM10"
    private String PM10;
    //"PM0.5"
    private String PM05;
    //"PM5.0"
    private String PM50;
    //"N2压力"
    private String N2;
    //1：市电通->市电断 2：市电断->市电断 3：市电断->市电通  4：市电正常 5：市电异常
    private String UPS;
    //开关量编号（目前只支持两路开关量 1 和 2）
    private String DOOR;
    //开关量编号报警类型/状态 1：开->关 2：关->开 3：持续关 4：持续开
    private String DOORZ;
    //"甲醛")
    private String OX;
    //左舱室温度
    private String leftCompartmentTemp;
    //左舱室流量
    private String leftCompartmentFlow;
    //左舱室湿度
    private String leftCompartmentHumidity;
    //右舱室温度
    private String rightCompartmentTemp;
    //右舱室流量
    private String rightCompartmentFlow;
    //右舱室湿度
    private String rightCompartmentHumidity;
    //电压
    private String voltage;
    //电流
    private String current;
    //功率
    private String power;


    //"时间"
    private Date nowTime;
    //"通道id"
    private String channelId;

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public String getCmdid() {
        return cmdid;
    }

    public void setCmdid(String cmdid) {
        this.cmdid = cmdid;
    }

    public String getCO2() {
        return CO2;
    }

    public void setCO2(String CO2) {
        this.CO2 = CO2;
    }

    public String getO2() {
        return O2;
    }

    public void setO2(String o2) {
        O2 = o2;
    }

    public String getVOC() {
        return VOC;
    }

    public void setVOC(String VOC) {
        this.VOC = VOC;
    }

    public String getTEMP() {
        return TEMP;
    }

    public void setTEMP(String TEMP) {
        this.TEMP = TEMP;
    }

    public String getTEMP2() {
        return TEMP2;
    }

    public void setTEMP2(String TEMP2) {
        this.TEMP2 = TEMP2;
    }

    public String getTEMP3() {
        return TEMP3;
    }

    public void setTEMP3(String TEMP3) {
        this.TEMP3 = TEMP3;
    }

    public String getTEMP4() {
        return TEMP4;
    }

    public void setTEMP4(String TEMP4) {
        this.TEMP4 = TEMP4;
    }

    public String getTEMP5() {
        return TEMP5;
    }

    public void setTEMP5(String TEMP5) {
        this.TEMP5 = TEMP5;
    }

    public String getTEMP6() {
        return TEMP6;
    }

    public void setTEMP6(String TEMP6) {
        this.TEMP6 = TEMP6;
    }

    public String getTEMP7() {
        return TEMP7;
    }

    public void setTEMP7(String TEMP7) {
        this.TEMP7 = TEMP7;
    }

    public String getTEMP8() {
        return TEMP8;
    }

    public void setTEMP8(String TEMP8) {
        this.TEMP8 = TEMP8;
    }

    public String getTEMP9() {
        return TEMP9;
    }

    public void setTEMP9(String TEMP9) {
        this.TEMP9 = TEMP9;
    }

    public String getTEMP10() {
        return TEMP10;
    }

    public void setTEMP10(String TEMP10) {
        this.TEMP10 = TEMP10;
    }

    public String getAirflow() {
        return airflow;
    }

    public void setAirflow(String airflow) {
        this.airflow = airflow;
    }

    public String getRH() {
        return RH;
    }

    public void setRH(String RH) {
        this.RH = RH;
    }

    public String getPRESS() {
        return PRESS;
    }

    public void setPRESS(String PRESS) {
        this.PRESS = PRESS;
    }

    public String getQC() {
        return QC;
    }

    public void setQC(String QC) {
        this.QC = QC;
    }

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }

    public String getPM05() {
        return PM05;
    }

    public void setPM05(String PM05) {
        this.PM05 = PM05;
    }

    public String getPM50() {
        return PM50;
    }

    public void setPM50(String PM50) {
        this.PM50 = PM50;
    }

    public String getN2() {
        return N2;
    }

    public void setN2(String n2) {
        N2 = n2;
    }

    public String getUPS() {
        return UPS;
    }

    public void setUPS(String UPS) {
        this.UPS = UPS;
    }

    public String getDOOR() {
        return DOOR;
    }

    public void setDOOR(String DOOR) {
        this.DOOR = DOOR;
    }

    public String getDOORZ() {
        return DOORZ;
    }

    public void setDOORZ(String DOORZ) {
        this.DOORZ = DOORZ;
    }

    public String getOX() {
        return OX;
    }

    public void setOX(String OX) {
        this.OX = OX;
    }

    public String getLeftCompartmentTemp() {
        return leftCompartmentTemp;
    }

    public void setLeftCompartmentTemp(String leftCompartmentTemp) {
        this.leftCompartmentTemp = leftCompartmentTemp;
    }

    public String getLeftCompartmentFlow() {
        return leftCompartmentFlow;
    }

    public void setLeftCompartmentFlow(String leftCompartmentFlow) {
        this.leftCompartmentFlow = leftCompartmentFlow;
    }

    public String getLeftCompartmentHumidity() {
        return leftCompartmentHumidity;
    }

    public void setLeftCompartmentHumidity(String leftCompartmentHumidity) {
        this.leftCompartmentHumidity = leftCompartmentHumidity;
    }

    public String getRightCompartmentTemp() {
        return rightCompartmentTemp;
    }

    public void setRightCompartmentTemp(String rightCompartmentTemp) {
        this.rightCompartmentTemp = rightCompartmentTemp;
    }

    public String getRightCompartmentFlow() {
        return rightCompartmentFlow;
    }

    public void setRightCompartmentFlow(String rightCompartmentFlow) {
        this.rightCompartmentFlow = rightCompartmentFlow;
    }

    public String getRightCompartmentHumidity() {
        return rightCompartmentHumidity;
    }

    public void setRightCompartmentHumidity(String rightCompartmentHumidity) {
        this.rightCompartmentHumidity = rightCompartmentHumidity;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
