package com.hc.util;

import com.hc.application.ExcelMadel.HjExcleModel;
import com.hc.application.ExcelMadel.OtherExcleModel;
import com.hc.application.ExcelMadel.PyxExcleModel;
import com.hc.application.curvemodel.CurveDataModel;
import com.hc.application.curvemodel.SeriesDataModel;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.RegularUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class EquipmentInfoServiceHelp {


    public static  CurveInfoDto getCurveFirst(List<Monitorequipmentlastdata> lastDataModelList , Map<String,List<InstrumentParamConfigDto>> map,boolean flag){
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
        List<String> qc = new ArrayList<>();
        List<String> qcTime = new ArrayList<>();
        List<String> liquidLevel = new ArrayList<>();
        List<String> liquidLevelTime = new ArrayList<>();



//        defaultTime=lastDataModelList.get(0).getInputdatetime();
        for (int i = 0; i < lastDataModelList.size(); i++) {
            Monitorequipmentlastdata lastDataModel = lastDataModelList.get(i);
//            Date inputdatetime = lastDataModel.getInputdatetime();
//            if(!ObjectUtils.isEmpty(inputdatetime)){
//                long aLong = inputdatetime.getTime()-defaultTime.getTime();
//                if( i != 0 && aLong < halfAnHour){
//                    continue;
//                }
//                defaultTime = inputdatetime;
//            }
            //液氮罐是否存在差值
            String da = DateUtils.parseDatetime(lastDataModel.getInputdatetime());
            if(flag){
                da = DateUtils.paseDate(lastDataModel.getInputdatetime());
            }
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperaturediff())) {
                tempdiff.add(lastDataModel.getCurrenttemperaturediff());
                tempdiffTime.add(da);
            }
            //培养箱气流有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow1()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentairflow1())) {
                airflow.add(lastDataModel.getCurrentairflow1());
                airflowTime.add(da);
            }
            //是否存在左舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentlefttemperature()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentlefttemperature())) {
                lefttemp.add(lastDataModel.getCurrentlefttemperature());
                lefttempTime.add(da);
            }
            //是否存在右舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrigthtemperature()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentrigthtemperature())) {
                righttemp.add(lastDataModel.getCurrentrigthtemperature());
                righttempTime.add(da);
            }
            //温度1有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature1()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature1())) {
                temp1.add(lastDataModel.getCurrenttemperature1());
                temp1Time.add(da);
            }
            //温度2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature2()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature2())) {
                temp2.add(lastDataModel.getCurrenttemperature2());
                temp2Time.add(da);
            }
            //温度3有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature3()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature3())) {
                temp3.add(lastDataModel.getCurrenttemperature3());
                temp3Time.add(da);
            }
            //温度4有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature4()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature4())) {
                temp4.add(lastDataModel.getCurrenttemperature4());
                temp4Time.add(da);
            }
            //温度5有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature5()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature5())) {
                temp5.add(lastDataModel.getCurrenttemperature5());
                temp5Time.add(da);
            }
            //温度6有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature6()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature6())) {
                temp6.add(lastDataModel.getCurrenttemperature6());
                temp6Time.add(da);
            }
            //温度7有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature7()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature7())) {
                temp7.add(lastDataModel.getCurrenttemperature7());
                temp7Time.add(da);
            }
            //温度8有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature8()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature8())) {
                temp8.add(lastDataModel.getCurrenttemperature8());
                temp8Time.add(da);
            }
            //温度9有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature9()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature9())) {
                temp9.add(lastDataModel.getCurrenttemperature9());
                temp9Time.add(da);
            }
            //温度10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature10()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature10())) {
                temp10.add(lastDataModel.getCurrenttemperature10());
                temp10Time.add(da);
            }
            //温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenttemperature())) {
                temp.add(lastDataModel.getCurrenttemperature());
                tempTime.add(da);

            }
            //CO2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentcarbondioxide()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentcarbondioxide())) {
                CO2.add(lastDataModel.getCurrentcarbondioxide());
                CO2Time.add(da);
                //}
            }
            //氧气有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrento2()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrento2())) {
                O2.add(lastDataModel.getCurrento2());
                O2Time.add(da);
                //}
            }
            //voc 有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentvoc()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentvoc())) {
                VOC.add(lastDataModel.getCurrentvoc());
                VOCTime.add(da);
            }
            //湿度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenthumidity()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrenthumidity())) {
                RH.add(lastDataModel.getCurrenthumidity());
                RHTime.add(da);
            }
            //pm2.5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm25()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentpm25())) {
                PM25.add(lastDataModel.getCurrentpm25());
                PM25Time.add(da);
            }
            //pm5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm5()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentpm5())) {
                PM5.add(lastDataModel.getCurrentpm5());
                PM5Time.add(da);
            }
            //pm05有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm05()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentpm05())) {
                PM05.add(lastDataModel.getCurrentpm05());
                PM05Time.add(da);
            }
            //PM10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm10()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentpm10())) {
                PM10.add(lastDataModel.getCurrentpm10());
                PM10Time.add(da);
            }
            //甲醛有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentformaldehyde()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentformaldehyde())) {
                JQ.add(lastDataModel.getCurrentformaldehyde());
                JQTime.add(da);
            }
            //压力有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentairflow())) {
                PRESS.add(lastDataModel.getCurrentairflow());
                PRESSTime.add(da);
            }
            //左盖板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftcovertemperature()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentleftcovertemperature())) {
                leftcovertemp.add(lastDataModel.getCurrentleftcovertemperature());
                leftcovertempTime.add(da);
            }
            // 左底板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftendtemperature()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentleftendtemperature())) {
                leftendtemp.add(lastDataModel.getCurrentleftendtemperature());
                leftendTime.add(da);
            }
            //左气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftairflow()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentleftairflow())) {
                leftair.add(lastDataModel.getCurrentleftairflow());
                leftairTime.add(da);
            }
            //右盖板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightcovertemperature()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentrightcovertemperature())) {
                rightcovertemp.add(lastDataModel.getCurrentrightcovertemperature());
                rightcovertempTime.add(da);
            }
            //右底板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightendtemperature()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentrightendtemperature())) {
                rightendtemp.add(lastDataModel.getCurrentrightendtemperature());
                rightendtempTime.add(da);
            }
            //右气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightairflow()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentrightairflow())) {
                rightair.add(lastDataModel.getCurrentrightairflow());
                rightairTime.add(da);
            }
            //N2 有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentn2()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentn2())) {
                n2.add(lastDataModel.getCurrentn2());
                n2Time.add(da);
            }
            //左舱室湿度
            if (StringUtils.isNotEmpty(lastDataModel.getLeftCompartmentHumidity())&& RegularUtil.checkContainsNumbers(lastDataModel.getLeftCompartmentHumidity())) {
                leftCompartmentHumidity.add(lastDataModel.getLeftCompartmentHumidity());
                leftCompartmentHumidityTime.add(da);
            }
            //右舱室湿度
            if (StringUtils.isNotEmpty(lastDataModel.getRightCompartmentHumidity())&& RegularUtil.checkContainsNumbers(lastDataModel.getRightCompartmentHumidity())) {
                rightCompartmentHumidity.add(lastDataModel.getRightCompartmentHumidity());
                rightCompartmentHumidityTime.add(da);
            }
            //当前电量
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentqc()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentqc()) && map.containsKey("QC")) {
                qc.add(lastDataModel.getCurrentqc());
                qcTime.add(da);
            }
            //当前液位
            if(StringUtils.isNotEmpty(lastDataModel.getLiquidLevel())&& RegularUtil.checkContainsNumbers(lastDataModel.getLiquidLevel())){
                liquidLevel.add(lastDataModel.getLiquidLevel());
                liquidLevelTime.add(da);
            }

        }
        CurveInfoDto curveInfoDto = new CurveInfoDto();
        if (CollectionUtils.isNotEmpty(n2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(n2,n2Time,map.get("N2"));
            curveInfoDto.setN2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftcovertemp,leftcovertempTime,map.get("LEFTCOVERTEMP"));
            curveInfoDto.setLeftcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftendtemp,leftendTime,map.get("RIGHTENDTEMP"));
            curveInfoDto.setLeftendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftair,leftairTime,map.get("左气流"));
            curveInfoDto.setLeftair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightcovertemp,rightcovertempTime,map.get("RIGHTCOVERTEMP"));
            curveInfoDto.setRightcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightendtemp, rightendtempTime,map.get("RIGHTENDTEMP"));
            curveInfoDto.setRightendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightair, rightairTime,map.get("右气流"));
            curveInfoDto.setRightair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(tempdiff)) {
            CurveDataModel curveDataModel = generateCurveDataModel(tempdiff, tempdiffTime,map.get("DIFFTEMP"));
            curveInfoDto.setDifftemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(airflow)) {
            CurveDataModel curveDataModel = generateCurveDataModel(airflow, airflowTime,map.get("气流"));
            curveInfoDto.setAirflow(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(lefttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(lefttemp, lefttempTime,map.get("LEFTTEMP"));
            curveInfoDto.setLefttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(righttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(righttemp, righttempTime,map.get("RIGHTTEMP"));
            curveInfoDto.setRighttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp1)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp1, temp1Time,map.get("TEMP1"));
            curveInfoDto.setTemp1(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp2, temp2Time,map.get("TEMP2"));
            curveInfoDto.setTemp2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp3)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp3, temp3Time,map.get("TEMP3"));
            curveInfoDto.setTemp3(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp4)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp4, temp4Time,map.get("TEMP4"));
            curveInfoDto.setTemp4(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp5, temp5Time,map.get("TEMP5"));
            curveInfoDto.setTemp5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp6)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp6, temp6Time,map.get("TEMP6"));
            curveInfoDto.setTemp6(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp7)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp7, temp7Time,map.get("TEMP7"));
            curveInfoDto.setTemp7(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp8)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp8, temp8Time,map.get("TEMP8"));
            curveInfoDto.setTemp8(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp9)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp9, temp9Time,map.get("TEMP9"));
            curveInfoDto.setTemp9(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp10, temp10Time,map.get("TEMP10"));
            curveInfoDto.setTemp10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp, tempTime,map.get("TEMP"));
            curveInfoDto.setTemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(CO2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time,map.get("CO2"));
            curveInfoDto.setCo2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time,map.get("O2"));
            curveInfoDto.setO2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(VOC)) {
            CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime,map.get("VOC"));
            curveInfoDto.setVoc(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(RH)) {
            CurveDataModel curveDataModel = generateCurveDataModel(RH, RHTime,map.get("RH"));
            curveInfoDto.setRh(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM25)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM25, PM25Time,map.get("PM2.5"));
            curveInfoDto.setPm25(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM5, PM5Time,map.get("PM5"));
            curveInfoDto.setPm5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM05)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM05, PM05Time,map.get("PM0.5"));
            curveInfoDto.setPm05(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM10, PM10Time,map.get("PM10"));
            curveInfoDto.setPm10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(JQ)) {
            CurveDataModel curveDataModel = generateCurveDataModel(JQ, JQTime,map.get("甲醛"));
            curveInfoDto.setJq(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PRESS)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PRESS, PRESSTime,map.get("PRESS"));
            curveInfoDto.setPress(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftCompartmentHumidity, leftCompartmentHumidityTime,map.get("leftCompartmentHumidity"));
            curveInfoDto.setLeftCompartmentHumidity(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightCompartmentHumidity, rightCompartmentHumidityTime,map.get("rightCompartmentHumidity"));
            curveInfoDto.setRightCompartmentHumidity(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(qc)) {
            CurveDataModel curveDataModel = generateCurveDataModel(qc, qcTime,map.get("QC"));
            curveInfoDto.setQc(curveDataModel);
        }
        if(CollectionUtils.isNotEmpty(liquidLevel)){
            CurveDataModel curveDataModel = generateCurveDataModel(liquidLevel, liquidLevelTime, map.get("LIQUIDLEVEL"));
            curveInfoDto.setLiquidLevel(curveDataModel);
        }
        return curveInfoDto;
    }

    private static  CurveDataModel generateCurveDataModel(List<String> listdata, List<String> listtime,List<InstrumentParamConfigDto> list){
        CurveDataModel curveDataModel = new CurveDataModel();
        curveDataModel.setXaxis(listtime);
        SeriesDataModel seriesDataModel = new SeriesDataModel();
        seriesDataModel.setDate(listdata);
        if (CollectionUtils.isNotEmpty(list)) {
            curveDataModel.setMaxNum(list.get(0).getHighLimit()+"");
            curveDataModel.setMinNum(list.get(0).getLowLimit()+"");
            curveDataModel.setStyleMin(list.get(0).getLowLimit()+"");
            curveDataModel.setStyleMax(list.get(0).getStyleMax()+"");
        }else {
            OptionalDouble max = listdata.stream().mapToDouble(Double::parseDouble).max();
            if (max.isPresent()) {
                curveDataModel.setMaxNum(max.getAsDouble()+"");
                curveDataModel.setMaxNum(max.getAsDouble()+"");
            }else {
                curveDataModel.setMaxNum("");
                curveDataModel.setStyleMax("");
            }
            OptionalDouble min = listdata.stream().mapToDouble(Double::parseDouble).min();
            if (min.isPresent()) {
                curveDataModel.setMinNum(min.getAsDouble()+"");
                curveDataModel.setStyleMin(min.getAsDouble()+"");
            }else {
                curveDataModel.setMinNum("");
                curveDataModel.setStyleMin("");
            }
        }
        List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
        seriesDataModelList.add(seriesDataModel);
        curveDataModel.setSeries(seriesDataModelList);
        return  curveDataModel;
    }

    /**
     *
     * @param lastDataModelList LASTDATA集合
     * @param map  MAP集合 K为设备检测得名称（co2，O2等） v为对应得上下值
     * @param flag  改变时间返回值得类型
     * @return
     */
    public static CurveInfoDto getCurveFirstByMT300DC(List<Monitorequipmentlastdata> lastDataModelList, Map<String,List<InstrumentParamConfigDto>> map, boolean flag) {
        List<String> probe1Temp = new ArrayList<>();
        List<String> probe1TempTime = new ArrayList<>();
        List<String> probe1rh = new ArrayList<>();
        List<String> probe1rhTime = new ArrayList<>();
        List<String> probe1Co2 = new ArrayList<>();
        List<String> probe1Co2Time = new ArrayList<>();
        List<String> probe1O2 = new ArrayList<>();
        List<String> probe1O2Time = new ArrayList<>();
        List<String> probe2Temp = new ArrayList<>();
        List<String> probe2TempTime = new ArrayList<>();
        List<String> probe2rh = new ArrayList<>();
        List<String> probe2rhTime = new ArrayList<>();
        List<String> probe2Co2 = new ArrayList<>();
        List<String> probe2Co2Time = new ArrayList<>();
        List<String> probe2O2 = new ArrayList<>();
        List<String> probe2O2Time = new ArrayList<>();
        List<String> probe3Temp = new ArrayList<>();
        List<String> probe3TempTime = new ArrayList<>();
        List<String> probe3rh = new ArrayList<>();
        List<String> probe3rhTime = new ArrayList<>();
        List<String> probe3Co2 = new ArrayList<>();
        List<String> probe3Co2Time = new ArrayList<>();
        List<String> probe3O2 = new ArrayList<>();
        List<String> probe3O2Time = new ArrayList<>();
        List<String> CO2 = new ArrayList<String>();
        List<String> CO2Time = new ArrayList<String>();
        List<String> O2 = new ArrayList<String>();
        List<String> O2Time = new ArrayList<String>();
        List<String> VOC = new ArrayList<String>();
        List<String> VOCTime = new ArrayList<String>();
        for (int i = 0; i < lastDataModelList.size(); i++)
        {
            Monitorequipmentlastdata lastDataModel = lastDataModelList.get(i);
            String da = DateUtils.parseDatetime(lastDataModel.getInputdatetime());
            if(flag){
                da = DateUtils.paseDate(lastDataModel.getInputdatetime());
            }
            //CO2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentcarbondioxide()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentcarbondioxide())) {
                CO2.add(lastDataModel.getCurrentcarbondioxide());
                CO2Time.add(da);
                //}
            }
            //氧气有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrento2()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrento2())) {
                O2.add(lastDataModel.getCurrento2());
                O2Time.add(da);
                //}
            }
            //voc 有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentvoc()) && RegularUtil.checkContainsNumbers(lastDataModel.getCurrentvoc())) {
                VOC.add(lastDataModel.getCurrentvoc());
                VOCTime.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getProbe1model()) && RegularUtil.checkContainsNumbers(lastDataModel.getProbe1model())
                    && StringUtils.isNotEmpty(lastDataModel.getProbe1data()) && RegularUtil.checkContainsNumbers(lastDataModel.getProbe1data()))
            {
                switch (lastDataModel.getProbe1model()){
                    case "1":
                        probe1Temp.add(lastDataModel.getProbe1data());
                        probe1TempTime.add(da);
                        break;
                    case "2":
                        probe1rh.add(lastDataModel.getProbe1data());
                        probe1rhTime.add(da);
                        break;
                    case "3":
                        probe1O2.add(lastDataModel.getProbe1data());
                        probe1O2Time.add(da);
                        break;
                    case "4":
                        probe1Co2.add(lastDataModel.getProbe1data());
                        probe1Co2Time.add(da);
                        break;
                }
            }
            if (StringUtils.isNotEmpty(lastDataModel.getProbe2model()) && RegularUtil.checkContainsNumbers(lastDataModel.getProbe2model())
                    && StringUtils.isNotEmpty(lastDataModel.getProbe2data()) && RegularUtil.checkContainsNumbers(lastDataModel.getProbe2data()))
            {
                switch (lastDataModel.getProbe2model()){
                    case "1":
                        probe2Temp.add(lastDataModel.getProbe2data());
                        probe2TempTime.add(da);
                        break;
                    case "2":
                        probe2rh.add(lastDataModel.getProbe2data());
                        probe2rhTime.add(da);
                        break;
                    case "3":
                        probe2O2.add(lastDataModel.getProbe2data());
                        probe2O2Time.add(da);
                        break;
                    case "4":
                        probe2Co2.add(lastDataModel.getProbe2data());
                        probe2Co2Time.add(da);
                        break;
                }
            }
            if (StringUtils.isNotEmpty(lastDataModel.getProbe3model()) && RegularUtil.checkContainsNumbers(lastDataModel.getProbe3model())
                    && StringUtils.isNotEmpty(lastDataModel.getProbe3data()) && RegularUtil.checkContainsNumbers(lastDataModel.getProbe3data()))
            {
                switch (lastDataModel.getProbe3model()){
                    case "1":
                        probe3Temp.add(lastDataModel.getProbe3data());
                        probe3TempTime.add(da);
                        break;
                    case "2":
                        probe3rh.add(lastDataModel.getProbe3data());
                        probe3rhTime.add(da);
                        break;
                    case "3":
                        probe3O2.add(lastDataModel.getProbe3data());
                        probe3O2Time.add(da);
                        break;
                    case "4":
                        probe3Co2.add(lastDataModel.getProbe3data());
                        probe3Co2Time.add(da);
                        break;
                }
            }
        }
        CurveInfoDto curveInfoDto = new CurveInfoDto();
        if (CollectionUtils.isNotEmpty(CO2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time,map.get("CO2"));
            curveInfoDto.setCo2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time,map.get("O2"));
            curveInfoDto.setO2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(VOC)) {
            CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime,map.get("VOC"));
            curveInfoDto.setVoc(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe1Temp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe1Temp, probe1TempTime,map.get("TEMP"));
            curveInfoDto.setProbe1Temp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe1rh)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe1rh, probe1rhTime,map.get("RH"));
            curveInfoDto.setProbe1rh(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe1Co2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe1Co2, probe1Co2Time,map.get("CO2"));
            curveInfoDto.setProbe1Co2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe1O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe1O2, probe1O2Time,map.get("O2"));
            curveInfoDto.setProbe1O2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe2Temp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe2Temp, probe2TempTime,map.get("TEMP"));
            curveInfoDto.setProbe2Temp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe2rh)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe2rh, probe2rhTime,map.get("RH"));
            curveInfoDto.setProbe2rh(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe2Co2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe2Co2, probe2Co2Time,map.get("CO2"));
            curveInfoDto.setProbe2Co2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe2O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe2O2, probe2O2Time,map.get("O2"));
            curveInfoDto.setProbe2O2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe3Temp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe3Temp, probe3TempTime,map.get("TEMP"));
            curveInfoDto.setProbe3Temp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe3rh)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe3rh, probe3rhTime,map.get("RH"));
            curveInfoDto.setProbe3rh(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe3Co2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe3Co2, probe3Co2Time,map.get("CO2"));
            curveInfoDto.setProbe3Co2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(probe3O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(probe3O2, probe3O2Time,map.get("O2"));
            curveInfoDto.setProbe3O2(curveDataModel);
        }
        return curveInfoDto;
    }

    public static List<HjExcleModel> getHj(List<Monitorequipmentlastdata> list,String equipmentName) {
        List<HjExcleModel> hjExcleModelList = new ArrayList<>();
        for (Monitorequipmentlastdata sing : list) {
            HjExcleModel hjExcleModel = new HjExcleModel();
            Date inputdatetime = sing.getInputdatetime();
            String currenttemperature = sing.getCurrenttemperature();
            String currento2 = sing.getCurrento2();
            String currentcarbondioxide = sing.getCurrentcarbondioxide();
            String currentformaldehyde = sing.getCurrentformaldehyde();
            String currenthumidity = sing.getCurrenthumidity();
            String currentpm5 = sing.getCurrentpm5();
            String currentpm05 = sing.getCurrentpm05();
            String currentpm10 = sing.getCurrentpm10();
            String currentpm25 = sing.getCurrentpm25();
            String currentvoc = sing.getCurrentvoc();
            String currentairflow = sing.getCurrentairflow();
            if (StringUtils.isNotEmpty(currenttemperature)) {
                hjExcleModel.setCurrenttemperature(currenttemperature);
            }
            if (StringUtils.isNotEmpty(currento2)) {
                hjExcleModel.setCurrento2(currento2);
            }
            if (StringUtils.isNotEmpty(currentcarbondioxide)) {
                hjExcleModel.setCurrentcarbondioxide(currentcarbondioxide);
            }
            if (StringUtils.isNotEmpty(currentformaldehyde)) {
                hjExcleModel.setCurrentformaldehyde(currentformaldehyde);
            }
            if (StringUtils.isNotEmpty(currenthumidity)) {
                hjExcleModel.setCurrenthumidity(currenthumidity);
            }
            if (StringUtils.isNotEmpty(currentpm5)) {
                hjExcleModel.setCurrentpm5(currentpm5);
            }
            if (StringUtils.isNotEmpty(currentpm05)) {
                hjExcleModel.setCurrentpm05(currentpm05);
            }
            if (StringUtils.isNotEmpty(currentpm10)) {
                hjExcleModel.setCurrentpm10(currentpm10);
            }
            if (StringUtils.isNotEmpty(currentpm25)) {
                hjExcleModel.setCurrentpm25(currentpm25);
            }
            if (StringUtils.isNotEmpty(currentvoc)) {
                hjExcleModel.setCurrentvoc(currentvoc);
            }
            if (StringUtils.isNotEmpty(currentairflow)) {
                hjExcleModel.setCurrentairflow(currentairflow);
            }
            hjExcleModel.setInputdatetime(inputdatetime);
            hjExcleModel.setEquipmentname(equipmentName);
            hjExcleModelList.add(hjExcleModel);
        }

        return hjExcleModelList;
    }

    public static List<PyxExcleModel> getPyx(List<Monitorequipmentlastdata> monitorequipmentlastdataList,String equipmentName) {
        List<PyxExcleModel> hjExcleModelList = new ArrayList<>();
        for (Monitorequipmentlastdata sing : monitorequipmentlastdataList) {
            PyxExcleModel pyxExcleModel = new PyxExcleModel();
            List<String> list = new ArrayList<String>();
            list.add("A");
            list.add("B");
            list.add("C");
            list.add("D");
            list.add("E");
            list.add("F");
            Date inputdatetime = sing.getInputdatetime();
            String currenttemperature = sing.getCurrenttemperature();
            String currento2 = sing.getCurrento2();
            String currentcarbondioxide = sing.getCurrentcarbondioxide();
            String currenthumidity = sing.getCurrenthumidity();
            String currentairflow1 = sing.getCurrentairflow1();
            String currenttemperature1 = sing.getCurrenttemperature1();
            String currenttemperature2 = sing.getCurrenttemperature2();
            String currenttemperature3 = sing.getCurrenttemperature3();
            String currenttemperature4 = sing.getCurrenttemperature4();
            String currenttemperature5 = sing.getCurrenttemperature5();
            String currenttemperature6 = sing.getCurrenttemperature6();
            String currenttemperature7 = sing.getCurrenttemperature7();
            String currenttemperature8 = sing.getCurrenttemperature8();
            String currenttemperature9 = sing.getCurrenttemperature9();
            String currenttemperature10 = sing.getCurrenttemperature10();
            String currentleftcovertemperature = sing.getCurrentleftcovertemperature();
            String currentrightcovertemperature = sing.getCurrentrightcovertemperature();
            String currentleftendtemperature = sing.getCurrentleftendtemperature();
            String currentrightendtemperature = sing.getCurrentrightendtemperature();
            String currentlefttemperature = sing.getCurrentlefttemperature();
            String currentrigthtemperature = sing.getCurrentrigthtemperature();
            if (StringUtils.isNotEmpty(currenttemperature)) {
                if (list.contains(currenttemperature)) {
                    currenttemperature = "探头获取数据异常";
                }
                pyxExcleModel.setCurrenttemperature(currenttemperature);
            }
            if (StringUtils.isNotEmpty(currento2)) {
                if (list.contains(currento2)) {
                    currento2 = "探头获取数据异常";
                }
                pyxExcleModel.setCurrento2(currento2);
            }
            if (StringUtils.isNotEmpty(currentcarbondioxide)) {
                if (list.contains(currentcarbondioxide)) {
                    currentcarbondioxide = "探头获取数据异常";
                }
                pyxExcleModel.setCurrentcarbondioxide(currentcarbondioxide);
            }
            if (StringUtils.isNotEmpty(currenttemperature1)) {
                pyxExcleModel.setCurrenttemperature1(currenttemperature1);
            }
            if (StringUtils.isNotEmpty(currenttemperature2)) {
                pyxExcleModel.setCurrenttemperature2(currenttemperature2);
            }
            if (StringUtils.isNotEmpty(currenttemperature3)) {
                pyxExcleModel.setCurrenttemperature3(currenttemperature3);
            }
            if (StringUtils.isNotEmpty(currenttemperature4)) {
                pyxExcleModel.setCurrenttemperature4(currenttemperature4);
            }
            if (StringUtils.isNotEmpty(currenttemperature5)) {
                pyxExcleModel.setCurrenttemperature5(currenttemperature5);
            }
            if (StringUtils.isNotEmpty(currenttemperature6)) {
                pyxExcleModel.setCurrenttemperature6(currenttemperature6);
            }
            if (StringUtils.isNotEmpty(currenttemperature7)) {
                pyxExcleModel.setCurrenttemperature7(currenttemperature7);
            }
            if (StringUtils.isNotEmpty(currenttemperature8)) {
                pyxExcleModel.setCurrenttemperature8(currenttemperature8);
            }
            if (StringUtils.isNotEmpty(currenttemperature9)) {
                pyxExcleModel.setCurrenttemperature9(currenttemperature9);
            }
            if (StringUtils.isNotEmpty(currenttemperature10)) {
                pyxExcleModel.setCurrenttemperature10(currenttemperature10);
            }
            if (StringUtils.isNotEmpty(currentleftcovertemperature)) {
                pyxExcleModel.setCurrentleftcovertemperature(currentleftcovertemperature);
            }
            if (StringUtils.isNotEmpty(currentrightcovertemperature)) {
                pyxExcleModel.setCurrentrightcovertemperature(currentrightcovertemperature);
            }
            if (StringUtils.isNotEmpty(currentleftendtemperature)) {
                pyxExcleModel.setCurrentleftendtemperature(currentleftendtemperature);
            }
            if (StringUtils.isNotEmpty(currentrightendtemperature)) {
                pyxExcleModel.setCurrentrightendtemperature(currentrightendtemperature);
            }
            if (StringUtils.isNotEmpty(currenthumidity)) {
                pyxExcleModel.setCurrenthumidity(currenthumidity);
            }
            if (StringUtils.isNotEmpty(currentairflow1)) {
                if (list.contains(currentairflow1)) {
                    currentairflow1 = "设备获取数据异常";
                }
                pyxExcleModel.setCurrentairflow1(currentairflow1);
            }
            if (StringUtils.isNotEmpty(currentlefttemperature)) {
                if (list.contains(currentlefttemperature)) {
                    currentlefttemperature = "设备获取数据异常";
                }
                pyxExcleModel.setCurrentlefttemperature(currentlefttemperature);
            }
            if (StringUtils.isNotEmpty(currentrigthtemperature)) {
                if (list.contains(currentrigthtemperature)) {
                    currentrigthtemperature = "设备获取数据异常";
                }
                pyxExcleModel.setCurrentrigthtemperature(currentrigthtemperature);
            }
            if(StringUtils.isNotBlank(sing.getProbe1model()) || StringUtils.isNotBlank(sing.getProbe2model()) || StringUtils.isNotBlank(sing.getProbe3model())
            || StringUtils.isNotBlank(sing.getProbe1data()) || StringUtils.isNotBlank(sing.getProbe2data()) || StringUtils.isNotBlank(sing.getProbe3data()) ){
                pyxExcleModel.setModel(sing.getModel()==null?"":sing.getModel());
                pyxExcleModel.setProbe1model(sing.getProbe1model());
                pyxExcleModel.setProbe1data(sing.getProbe1data());
                pyxExcleModel.setProbe2model(sing.getProbe2model());
                pyxExcleModel.setProbe2data(sing.getProbe2data());
                pyxExcleModel.setModel(sing.getProbe3model());
                pyxExcleModel.setProbe3data(sing.getProbe3data());
            }
            pyxExcleModel.setInputdatetime(inputdatetime);
            pyxExcleModel.setEquipmentname(equipmentName);
            hjExcleModelList.add(pyxExcleModel);
        }
        return hjExcleModelList;
    }

    public static List<OtherExcleModel> getOther(List<Monitorequipmentlastdata> monitorequipmentlastdataList,String equipmentName) {
        List<OtherExcleModel> excleModelList = new ArrayList<>();
        for (Monitorequipmentlastdata sing : monitorequipmentlastdataList) {

            OtherExcleModel otherExcleModel = new OtherExcleModel();
            List<String> list = new ArrayList<String>();
            list.add("A");
            list.add("B");
            list.add("C");
            list.add("D");
            list.add("E");
            list.add("F");

            Date inputdatetime = sing.getInputdatetime();
            String currenttemperature = sing.getCurrenttemperature();
            if (StringUtils.isNotEmpty(currenttemperature)) {
                if (list.contains(currenttemperature)) {
                    currenttemperature = "探头获取数据异常";
                }
                otherExcleModel.setCurrenttemperature(currenttemperature);
            }
            otherExcleModel.setInputdatetime(inputdatetime);
            otherExcleModel.setEquipmentname(equipmentName);
            excleModelList.add(otherExcleModel);
        }
        return excleModelList;
    }

}
