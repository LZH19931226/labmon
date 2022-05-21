package com.hc.util;

import com.hc.application.curvemodel.CurveDataModel;
import com.hc.application.curvemodel.SeriesDataModel;
import com.hc.dto.CurveInfoDto;
import com.hc.vo.labmon.model.MonitorEquipmentLastDataModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EquipmentInfoServiceHelp {

    private static List<String> exceptionData = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q");

    public static  CurveInfoDto getCurveFirst(List<MonitorEquipmentLastDataModel> lastDataModelList, CurveInfoDto curveInfoDto, String equipmentName ){
        List<String> legend = new ArrayList<String>();
        legend.add(equipmentName);
        List<String> temp = new ArrayList<String>();
        List<String> tempTime = new ArrayList<String>();
        List<String> CO2 = new ArrayList<String>();
        List<String> CO2Time = new ArrayList<String>();
        List<String> O2 = new ArrayList<String>();
        List<String> O2Time = new ArrayList<String>();
        List<String> VOC = new ArrayList<String>();
        List<String> VOCTime = new ArrayList<String>();
        List<String> RH = new ArrayList<String>();
        List<String> RHTime = new ArrayList<String>();
        List<String> PM5 = new ArrayList<String>();
        List<String> PM5Time = new ArrayList<String>();
        List<String> PM05 = new ArrayList<String>();
        List<String> PM05Time = new ArrayList<String>();
        List<String> PM25 = new ArrayList<String>();
        List<String> PM25Time = new ArrayList<String>();
        List<String> PM10 = new ArrayList<String>();
        List<String> PM10Time = new ArrayList<String>();
        List<String> JQ = new ArrayList<String>();
        List<String> JQTime = new ArrayList<String>();
        List<String> PRESS = new ArrayList<String>();
        List<String> PRESSTime = new ArrayList<String>();
        List<String> airflow = new ArrayList<String>();
        List<String> airflowTime = new ArrayList<String>();
        List<String> lefttemp = new ArrayList<String>();
        List<String> lefttempTime = new ArrayList<String>();
        List<String> righttemp = new ArrayList<String>();
        List<String> righttempTime = new ArrayList<String>();
        List<String> temp1 = new ArrayList<String>();
        List<String> temp1Time = new ArrayList<String>();
        List<String> temp2 = new ArrayList<String>();
        List<String> temp2Time = new ArrayList<String>();
        List<String> temp3 = new ArrayList<String>();
        List<String> temp3Time = new ArrayList<String>();
        List<String> temp4 = new ArrayList<String>();
        List<String> temp4Time = new ArrayList<String>();
        List<String> temp5 = new ArrayList<String>();
        List<String> temp5Time = new ArrayList<String>();
        List<String> temp6 = new ArrayList<String>();
        List<String> temp6Time = new ArrayList<String>();
        List<String> temp7 = new ArrayList<String>();
        List<String> temp7Time = new ArrayList<String>();
        List<String> temp8 = new ArrayList<String>();
        List<String> temp8Time = new ArrayList<String>();
        List<String> temp9 = new ArrayList<String>();
        List<String> temp9Time = new ArrayList<String>();
        List<String> temp10 = new ArrayList<String>();
        List<String> temp10Time = new ArrayList<String>();
        List<String> tempdiff = new ArrayList<String>();
        List<String> tempdiffTime = new ArrayList<String>();
        List<String> leftcovertemp = new ArrayList<String>();
        List<String> leftcovertempTime = new ArrayList<String>();
        List<String> leftendtemp = new ArrayList<String>();
        List<String> leftendTime = new ArrayList<String>();
        List<String> leftair = new ArrayList<String>();
        List<String> leftairTime = new ArrayList<String>();
        List<String> rightcovertemp = new ArrayList<String>();
        List<String> rightcovertempTime = new ArrayList<String>();
        List<String> rightendtemp = new ArrayList<String>();
        List<String> rightendtempTime = new ArrayList<String>();
        List<String> rightair = new ArrayList<String>();
        List<String> rightairTime = new ArrayList<String>();
        List<String> n2 = new ArrayList<String>();
        List<String> n2Time = new ArrayList<String>();
        List<String> leftCompartmentHumidity = new ArrayList<String>();
        List<String> leftCompartmentHumidityTime = new ArrayList<String>();
        List<String> rightCompartmentHumidity = new ArrayList<String>();
        List<String> rightCompartmentHumidityTime = new ArrayList<String>();

        for (MonitorEquipmentLastDataModel lastDataModel : lastDataModelList) {
            //液氮罐是否存在差值
            SimpleDateFormat dateFormats = new SimpleDateFormat("HH:mm");
            String da = dateFormats.format(lastDataModel.getInputdatetime());
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperaturediff())) {
                tempdiff.add(lastDataModel.getCurrenttemperaturediff());
                tempdiffTime.add(da);
            }
            //培养箱气流有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow1()) && !exceptionData.contains(lastDataModel.getCurrentairflow1())) {
                airflow.add(lastDataModel.getCurrentairflow1());
                airflowTime.add(da);
            }
            //是否存在左舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentlefttemperature()) && !exceptionData.contains(lastDataModel.getCurrentlefttemperature())) {
                lefttemp.add(lastDataModel.getCurrentlefttemperature());
                lefttempTime.add(da);
            }
            //是否存在右舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrigthtemperature()) && !exceptionData.contains(lastDataModel.getCurrentrigthtemperature())) {
                righttemp.add(lastDataModel.getCurrentrigthtemperature());
                righttempTime.add(da);
            }
            //温度1有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature1()) && !exceptionData.contains(lastDataModel.getCurrenttemperature1())) {
                temp1.add(lastDataModel.getCurrenttemperature1());
                temp1Time.add(da);
            }
            //温度2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature2()) && !exceptionData.contains(lastDataModel.getCurrenttemperature2())) {
                temp2.add(lastDataModel.getCurrenttemperature2());
                temp2Time.add(da);
            }
            //温度3有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature3()) && !exceptionData.contains(lastDataModel.getCurrenttemperature3())) {
                temp3.add(lastDataModel.getCurrenttemperature3());
                temp3Time.add(da);
            }
            //温度4有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature4()) && !exceptionData.contains(lastDataModel.getCurrenttemperature4())) {
                temp4.add(lastDataModel.getCurrenttemperature4());
                temp4Time.add(da);
            }
            //温度5有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature5()) && !exceptionData.contains(lastDataModel.getCurrenttemperature5())) {
                temp5.add(lastDataModel.getCurrenttemperature5());
                temp5Time.add(da);
            }
            //温度6有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature6()) && !exceptionData.contains(lastDataModel.getCurrenttemperature6())) {
                temp6.add(lastDataModel.getCurrenttemperature6());
                temp6Time.add(da);
            }
            //温度7有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature7()) && !exceptionData.contains(lastDataModel.getCurrenttemperature7())) {
                temp7.add(lastDataModel.getCurrenttemperature7());
                temp7Time.add(da);
            }
            //温度8有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature8()) && !exceptionData.contains(lastDataModel.getCurrenttemperature8())) {
                temp8.add(lastDataModel.getCurrenttemperature8());
                temp8Time.add(da);
            }
            //温度9有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature9()) && !exceptionData.contains(lastDataModel.getCurrenttemperature9())) {
                temp9.add(lastDataModel.getCurrenttemperature9());
                temp9Time.add(da);
            }
            //温度10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature10()) && !exceptionData.contains(lastDataModel.getCurrenttemperature10())) {
                temp10.add(lastDataModel.getCurrenttemperature10());
                temp10Time.add(da);
            }
            //温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature()) && !exceptionData.contains(lastDataModel.getCurrenttemperature())) {
                temp.add(lastDataModel.getCurrenttemperature());
                tempTime.add(da);

            }
            //CO2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentcarbondioxide()) && !exceptionData.contains(lastDataModel.getCurrentcarbondioxide())) {
                CO2.add(lastDataModel.getCurrentcarbondioxide());
                CO2Time.add(da);
                //}
            }
            //氧气有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrento2()) && !exceptionData.contains(lastDataModel.getCurrento2())) {
                O2.add(lastDataModel.getCurrento2());
                O2Time.add(da);
                //}
            }
            //voc 有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentvoc()) && !exceptionData.contains(lastDataModel.getCurrentvoc())) {
                VOC.add(lastDataModel.getCurrentvoc());
                VOCTime.add(da);
            }
            //湿度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenthumidity()) && !exceptionData.contains(lastDataModel.getCurrenthumidity())) {
                RH.add(lastDataModel.getCurrenthumidity());
                RHTime.add(da);
            }
            //pm2.5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm25()) && !exceptionData.contains(lastDataModel.getCurrentpm25())) {
                PM25.add(lastDataModel.getCurrentpm25());
                PM25Time.add(da);
            }
            //pm5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm5()) && !exceptionData.contains(lastDataModel.getCurrentpm5())) {
                PM5.add(lastDataModel.getCurrentpm5());
                PM5Time.add(da);
            }
            //pm05有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm05()) && !exceptionData.contains(lastDataModel.getCurrentpm05())) {
                PM05.add(lastDataModel.getCurrentpm05());
                PM05Time.add(da);
            }
            //PM10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm10()) && !exceptionData.contains(lastDataModel.getCurrentpm10())) {
                PM10.add(lastDataModel.getCurrentpm10());
                PM10Time.add(da);
            }
            //甲醛有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentformaldehyde()) && !exceptionData.contains(lastDataModel.getCurrentformaldehyde())) {
                JQ.add(lastDataModel.getCurrentformaldehyde());
                JQTime.add(da);
            }
            //压力有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow()) && !exceptionData.contains(lastDataModel.getCurrentairflow())) {
                PRESS.add(lastDataModel.getCurrentairflow());
                PRESSTime.add(da);
            }
            //左盖板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftcovertemperature()) && !exceptionData.contains(lastDataModel.getCurrentleftcovertemperature())) {
                leftcovertemp.add(lastDataModel.getCurrentleftcovertemperature());
                leftcovertempTime.add(da);
            }
            // 左底板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftendtemperature()) && !exceptionData.contains(lastDataModel.getCurrentleftendtemperature())) {
                leftendtemp.add(lastDataModel.getCurrentleftendtemperature());
                leftendTime.add(da);
            }
            //左气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftairflow()) && !exceptionData.contains(lastDataModel.getCurrentleftairflow())) {
                leftair.add(lastDataModel.getCurrentleftairflow());
                leftairTime.add(da);
            }
            //右盖板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightcovertemperature())) {
                rightcovertemp.add(lastDataModel.getCurrentrightcovertemperature());
                rightcovertempTime.add(da);
            }
            //右底板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightendtemperature()) && !exceptionData.contains(lastDataModel.getCurrentrightendtemperature())) {
                rightendtemp.add(lastDataModel.getCurrentrightendtemperature());
                rightendtempTime.add(da);
            }
            //右气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightairflow()) && !exceptionData.contains(lastDataModel.getCurrentrightairflow())) {
                rightair.add(lastDataModel.getCurrentrightairflow());
                rightairTime.add(da);
            }
            //N2 有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentn2()) && !exceptionData.contains(lastDataModel.getCurrentn2())) {
                n2.add(lastDataModel.getCurrentn2());
                n2Time.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getLeftCompartmentHumidity())&& !exceptionData.contains(lastDataModel.getLeftCompartmentHumidity())) {
                leftCompartmentHumidity.add(lastDataModel.getLeftCompartmentHumidity());
                leftCompartmentHumidityTime.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getRightCompartmentHumidity())&& !exceptionData.contains(lastDataModel.getRightCompartmentHumidity())) {
                rightCompartmentHumidity.add(lastDataModel.getRightCompartmentHumidity());
                rightCompartmentHumidityTime.add(da);
            }
        }
        if (CollectionUtils.isNotEmpty(n2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(n2,n2Time);
            curveInfoDto.setN2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftcovertemp,leftcovertempTime);
            curveInfoDto.setLeftcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftendtemp,leftendTime);
            curveInfoDto.setLeftendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftair,leftairTime);
            curveInfoDto.setLeftair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightcovertemp,rightcovertempTime);
            curveInfoDto.setRightcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightendtemp, rightendtempTime);
            curveInfoDto.setRightendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightair, rightairTime);
            curveInfoDto.setRightair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(tempdiff)) {
            CurveDataModel curveDataModel = generateCurveDataModel(tempdiff, tempdiffTime);
            curveInfoDto.setDifftemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(airflow)) {
            CurveDataModel curveDataModel = generateCurveDataModel(airflow, airflowTime);
            curveInfoDto.setAirflow(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(lefttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(lefttemp, lefttempTime);
            curveInfoDto.setLefttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(righttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(righttemp, righttempTime);
            curveInfoDto.setRighttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp1)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp1, temp1Time);
            curveInfoDto.setTemp1(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp2, temp2Time);
            curveInfoDto.setTemp2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp3)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp3, temp3Time);
            curveInfoDto.setTemp3(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp4)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp4, temp4Time);
            curveInfoDto.setTemp4(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp5, temp5Time);
            curveInfoDto.setTemp5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp6)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp6, temp6Time);
            curveInfoDto.setTemp6(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp7)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp7, temp7Time);
            curveInfoDto.setTemp7(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp8)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp8, temp8Time);
            curveInfoDto.setTemp8(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp9)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp9, temp9Time);
            curveInfoDto.setTemp9(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp10, temp10Time);
            curveInfoDto.setTemp10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp, tempTime);
            curveInfoDto.setTemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(CO2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time);
            curveInfoDto.setCo2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time);
            curveInfoDto.setO2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(VOC)) {
            CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime);
            curveInfoDto.setVoc(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(RH)) {
            CurveDataModel curveDataModel = generateCurveDataModel(RH, RHTime);
            curveInfoDto.setRh(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM25)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM25, PM25Time);
            curveInfoDto.setPm25(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM5, PM5Time);
            curveInfoDto.setPm5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM05)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM05, PM05Time);
            curveInfoDto.setPm05(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM10, PM10Time);
            curveInfoDto.setPm10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(JQ)) {
            CurveDataModel curveDataModel = generateCurveDataModel(JQ, JQTime);
            curveInfoDto.setJq(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PRESS)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PRESS, PRESSTime);
            curveInfoDto.setPress(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftCompartmentHumidity, leftCompartmentHumidityTime);
            curveInfoDto.setLeftCompartmentHumidity(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightCompartmentHumidity, rightCompartmentHumidityTime);
            curveInfoDto.setRightCompartmentHumidity(curveDataModel);
        }
        return curveInfoDto;
    }

    private static  CurveDataModel generateCurveDataModel(List<String> listdata, List<String> listtime){
        CurveDataModel curveDataModel = new CurveDataModel();
        curveDataModel.setXaxis(listtime);
        SeriesDataModel seriesDataModel = new SeriesDataModel();
        seriesDataModel.setDate(listdata);
        List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
        seriesDataModelList.add(seriesDataModel);
        curveDataModel.setSeries(seriesDataModelList);
        return  curveDataModel;
    }
}
