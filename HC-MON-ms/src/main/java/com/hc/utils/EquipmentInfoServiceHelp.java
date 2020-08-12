package com.hc.utils;

import com.hc.entity.Monitorequipmentlastdata;
import com.hc.model.CurveDataModel;
import com.hc.model.CurveInfoModel;
import com.hc.model.SeriesDataModel;
import com.hc.model.SingleTimeExcle.HjExcleModel;
import com.hc.model.SingleTimeExcle.OtherExcleModel;
import com.hc.model.SingleTimeExcle.PyxExcleModel;
import com.hc.model.SingleTimeExcle.SingleTimeEquipmentModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/8/12 15:03
 * 描述:
 **/
public class EquipmentInfoServiceHelp {

   private static List<String> exceptionData = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q");

   public static  void getCurvelFirst( List<Monitorequipmentlastdata> lastDataModelList, CurveInfoModel curveInfoModel,String equipmentname  ){
       List<String> legend = new ArrayList<String>();
       legend.add(equipmentname);
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
       for (Monitorequipmentlastdata lastDataModel : lastDataModelList) {
           //液氮罐是否存在差值
           String da = TimeHelper.formats(lastDataModel.getInputdatetime());
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
           curveInfoModel.setN2(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(leftcovertemp)) {
           CurveDataModel curveDataModel = generateCurveDataModel(leftcovertemp,leftcovertempTime);
           curveInfoModel.setLeftcovertemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(leftendtemp)) {
           CurveDataModel curveDataModel = generateCurveDataModel(leftendtemp,leftendTime);
           curveInfoModel.setLeftendtemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(leftair)) {
           CurveDataModel curveDataModel = generateCurveDataModel(leftair,leftairTime);
           curveInfoModel.setLeftair(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(rightcovertemp)) {
           CurveDataModel curveDataModel = generateCurveDataModel(rightcovertemp,rightcovertempTime);
           curveInfoModel.setRightcovertemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(rightendtemp)) {
           CurveDataModel curveDataModel = generateCurveDataModel(rightendtemp, rightendtempTime);
           curveInfoModel.setRightendtemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(rightair)) {
           CurveDataModel curveDataModel = generateCurveDataModel(rightair, rightairTime);
           curveInfoModel.setRightair(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(tempdiff)) {
           CurveDataModel curveDataModel = generateCurveDataModel(tempdiff, tempdiffTime);
           curveInfoModel.setDifftemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(airflow)) {
           CurveDataModel curveDataModel = generateCurveDataModel(airflow, airflowTime);
           curveInfoModel.setAirflow(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(lefttemp)) {
           CurveDataModel curveDataModel = generateCurveDataModel(lefttemp, lefttempTime);
           curveInfoModel.setLefttemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(righttemp)) {
           CurveDataModel curveDataModel = generateCurveDataModel(righttemp, righttempTime);
           curveInfoModel.setRighttemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp1)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp1, temp1Time);
           curveInfoModel.setTemp1(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp2)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp2, temp2Time);
           curveInfoModel.setTemp2(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp3)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp3, temp3Time);
           curveInfoModel.setTemp3(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp4)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp4, temp4Time);
           curveInfoModel.setTemp4(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp5)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp5, temp5Time);
           curveInfoModel.setTemp5(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp6)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp6, temp6Time);
           curveInfoModel.setTemp6(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp7)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp7, temp7Time);
           curveInfoModel.setTemp7(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp8)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp8, temp8Time);
           curveInfoModel.setTemp8(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp9)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp9, temp9Time);
           curveInfoModel.setTemp9(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp10)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp10, temp10Time);
           curveInfoModel.setTemp10(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(temp)) {
           CurveDataModel curveDataModel = generateCurveDataModel(temp, tempTime);
           curveInfoModel.setTemp(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(CO2)) {
           CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time);
           curveInfoModel.setCo2(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(O2)) {
           CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time);
           curveInfoModel.setO2(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(VOC)) {
           CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime);
           curveInfoModel.setVoc(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(RH)) {
           CurveDataModel curveDataModel = generateCurveDataModel(RH, RHTime);
           curveInfoModel.setRh(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(PM25)) {
           CurveDataModel curveDataModel = generateCurveDataModel(PM25, PM25Time);
           curveInfoModel.setPm25(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(PM5)) {
           CurveDataModel curveDataModel = generateCurveDataModel(PM5, PM5Time);
           curveInfoModel.setPm5(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(PM05)) {
           CurveDataModel curveDataModel = generateCurveDataModel(PM05, PM05Time);
           curveInfoModel.setPm05(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(PM10)) {
           CurveDataModel curveDataModel = generateCurveDataModel(PM10, PM10Time);
           curveInfoModel.setPm10(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(JQ)) {
           CurveDataModel curveDataModel = generateCurveDataModel(JQ, JQTime);
           curveInfoModel.setJq(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(PRESS)) {
           CurveDataModel curveDataModel = generateCurveDataModel(PRESS, PRESSTime);
           curveInfoModel.setPress(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(leftCompartmentHumidity)) {
           CurveDataModel curveDataModel = generateCurveDataModel(leftCompartmentHumidity, leftCompartmentHumidityTime);
           curveInfoModel.setLeftCompartmentHumidity(curveDataModel);
       }
       if (CollectionUtils.isNotEmpty(rightCompartmentHumidity)) {
           CurveDataModel curveDataModel = generateCurveDataModel(rightCompartmentHumidity, rightCompartmentHumidityTime);
           curveInfoModel.setRightCompartmentHumidity(curveDataModel);
       }
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

    public static void getCurveInfoByMonthTime( List<SingleTimeEquipmentModel> info, CurveInfoModel curveInfoModel){
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
        for (SingleTimeEquipmentModel lastDataModel : info) {
            //       lastDataModel.setInputdatetime(TimeHelper.formats(lastDataModel.getInputdatetime()));
            //液氮罐是否存在差值
            String da = lastDataModel.getInputdatetime();
            //培养箱气流有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow1())) {
                airflow.add(lastDataModel.getCurrentairflow1());
                airflowTime.add(da);
            }
            //是否存在左舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentlefttemperature())) {
                lefttemp.add(lastDataModel.getCurrentlefttemperature());
                lefttempTime.add(da);
            }
            //是否存在右舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrigthtemperature())) {
                righttemp.add(lastDataModel.getCurrentrigthtemperature());
                righttempTime.add(da);
            }
            //温度1有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature1())) {
                temp1.add(lastDataModel.getCurrenttemperature1());
                temp1Time.add(da);
            }
            //温度2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature2())) {
                temp2.add(lastDataModel.getCurrenttemperature2());
                temp2Time.add(da);
            }
            //温度3有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature3())) {
                temp3.add(lastDataModel.getCurrenttemperature3());
                temp3Time.add(da);
            }
            //温度4有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature4())) {
                temp4.add(lastDataModel.getCurrenttemperature4());
                temp4Time.add(da);
            }
            //温度5有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature5())) {
                temp5.add(lastDataModel.getCurrenttemperature5());
                temp5Time.add(da);
            }
            //温度6有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature6())) {
                temp6.add(lastDataModel.getCurrenttemperature6());
                temp6Time.add(da);
            }
            //温度7有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature7())) {
                temp7.add(lastDataModel.getCurrenttemperature7());
                temp7Time.add(da);
            }
            //温度8有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature8())) {
                temp8.add(lastDataModel.getCurrenttemperature8());
                temp8Time.add(da);
            }
            //温度9有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature9())) {
                temp9.add(lastDataModel.getCurrenttemperature9());
                temp9Time.add(da);
            }
            //温度10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature10())) {
                temp10.add(lastDataModel.getCurrenttemperature10());
                temp10Time.add(da);
            }
            //温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature())) {
                // if (!"A".equals() && !"B".equals(lastDataModel.getCurrenttemperature()) && !"C".equals(lastDataModel.getCurrenttemperature()) && !"D".equals(lastDataModel.getCurrenttemperature()) && !"E".equals(lastDataModel.getCurrenttemperature())) {
                temp.add(lastDataModel.getCurrenttemperature());
                tempTime.add(da);

            }

            //CO2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentcarbondioxide())) {
                //if (!"A".equals(lastDataModel.getCurrentcarbondioxide()) && !"B".equals(lastDataModel.getCurrentcarbondioxide()) && !"C".equals(lastDataModel.getCurrentcarbondioxide()) && !"D".equals(lastDataModel.getCurrentcarbondioxide()) && !"E".equals(lastDataModel.getCurrentcarbondioxide())) {
                CO2.add(lastDataModel.getCurrentcarbondioxide());
                CO2Time.add(da);
                //}
            }
            //氧气有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrento2())) {
                // if (!"A".equals(lastDataModel.getCurrento2()) && !"B".equals(lastDataModel.getCurrento2()) && !"C".equals(lastDataModel.getCurrento2()) && !"D".equals(lastDataModel.getCurrento2()) && !"E".equals(lastDataModel.getCurrento2())) {
                O2.add(lastDataModel.getCurrento2());
                O2Time.add(da);
                //}
            }
            //voc 有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentvoc())) {
                VOC.add(lastDataModel.getCurrentvoc());
                VOCTime.add(da);
            }
            //湿度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenthumidity())) {
                RH.add(lastDataModel.getCurrenthumidity());
                RHTime.add(da);
            }
            //pm2.5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm25())) {
                PM25.add(lastDataModel.getCurrentpm25());
                PM25Time.add(da);
            }
            //pm5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm5())) {
                PM5.add(lastDataModel.getCurrentpm5());
                PM5Time.add(da);
            }
            //pm05有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm05())) {
                PM05.add(lastDataModel.getCurrentpm05());
                PM05Time.add(da);
            }
            //PM10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm10())) {
                PM10.add(lastDataModel.getCurrentpm10());
                PM10Time.add(da);
            }
            //甲醛有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentformaldehyde())) {
                JQ.add(lastDataModel.getCurrentformaldehyde());
                JQTime.add(da);
            }
            //压力有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow())) {
                PRESS.add(lastDataModel.getCurrentairflow());
                PRESSTime.add(da);
            }
            //左盖板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftcovertemperature())) {
                leftcovertemp.add(lastDataModel.getCurrentleftcovertemperature());
                leftcovertempTime.add(da);
            }
            // 左底板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftendtemperature())) {
                leftendtemp.add(lastDataModel.getCurrentleftendtemperature());
                leftendTime.add(da);
            }
            //左气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftairflow())) {
                leftair.add(lastDataModel.getCurrentleftairflow());
                leftairTime.add(da);
            }
            //右盖板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightcovertemperature())) {
                rightcovertemp.add(lastDataModel.getCurrentrightcovertemperature());
                rightcovertempTime.add(da);
            }
            //右底板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightendtemperature())) {
                rightendtemp.add(lastDataModel.getCurrentrightendtemperature());
                rightendtempTime.add(da);
            }
            //右气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightairflow()) ) {
                rightair.add(lastDataModel.getCurrentrightairflow());
                rightairTime.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentn2())) {
                n2.add(lastDataModel.getCurrentn2());
                n2Time.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getLeftCompartmentHumidity())) {
                leftCompartmentHumidity.add(lastDataModel.getLeftCompartmentHumidity());
                leftCompartmentHumidityTime.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getRightCompartmentHumidity())) {
                rightCompartmentHumidity.add(lastDataModel.getRightCompartmentHumidity());
                rightCompartmentHumidityTime.add(da);
            }

        }
        if (CollectionUtils.isNotEmpty(n2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(n2,n2Time);
            curveInfoModel.setN2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftcovertemp,leftcovertempTime);
            curveInfoModel.setLeftcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftendtemp,leftendTime);
            curveInfoModel.setLeftendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftair,leftairTime);
            curveInfoModel.setLeftair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightcovertemp,rightcovertempTime);
            curveInfoModel.setRightcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightendtemp, rightendtempTime);
            curveInfoModel.setRightendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightair, rightairTime);
            curveInfoModel.setRightair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(tempdiff)) {
            CurveDataModel curveDataModel = generateCurveDataModel(tempdiff, tempdiffTime);
            curveInfoModel.setDifftemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(airflow)) {
            CurveDataModel curveDataModel = generateCurveDataModel(airflow, airflowTime);
            curveInfoModel.setAirflow(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(lefttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(lefttemp, lefttempTime);
            curveInfoModel.setLefttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(righttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(righttemp, righttempTime);
            curveInfoModel.setRighttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp1)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp1, temp1Time);
            curveInfoModel.setTemp1(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp2, temp2Time);
            curveInfoModel.setTemp2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp3)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp3, temp3Time);
            curveInfoModel.setTemp3(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp4)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp4, temp4Time);
            curveInfoModel.setTemp4(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp5, temp5Time);
            curveInfoModel.setTemp5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp6)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp6, temp6Time);
            curveInfoModel.setTemp6(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp7)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp7, temp7Time);
            curveInfoModel.setTemp7(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp8)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp8, temp8Time);
            curveInfoModel.setTemp8(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp9)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp9, temp9Time);
            curveInfoModel.setTemp9(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp10, temp10Time);
            curveInfoModel.setTemp10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp, tempTime);
            curveInfoModel.setTemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(CO2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time);
            curveInfoModel.setCo2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time);
            curveInfoModel.setO2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(VOC)) {
            CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime);
            curveInfoModel.setVoc(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(RH)) {
            CurveDataModel curveDataModel = generateCurveDataModel(RH, RHTime);
            curveInfoModel.setRh(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM25)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM25, PM25Time);
            curveInfoModel.setPm25(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM5, PM5Time);
            curveInfoModel.setPm5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM05)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM05, PM05Time);
            curveInfoModel.setPm05(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM10, PM10Time);
            curveInfoModel.setPm10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(JQ)) {
            CurveDataModel curveDataModel = generateCurveDataModel(JQ, JQTime);
            curveInfoModel.setJq(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PRESS)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PRESS, PRESSTime);
            curveInfoModel.setPress(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftCompartmentHumidity, leftCompartmentHumidityTime);
            curveInfoModel.setLeftCompartmentHumidity(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightCompartmentHumidity, rightCompartmentHumidityTime);
            curveInfoModel.setRightCompartmentHumidity(curveDataModel);
        }

    }


    public static OtherExcleModel getOther(SingleTimeEquipmentModel sing) {
        OtherExcleModel otherExcleModel = new OtherExcleModel();
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        String equipmentname = sing.getEquipmentname();
        String inputdatetime = sing.getInputdatetime();
        String currenttemperature = sing.getCurrenttemperature();
        if (StringUtils.isNotEmpty(currenttemperature)) {
            if (list.contains(currenttemperature)) {
                currenttemperature = "探头获取数据异常";
            }
            otherExcleModel.setCurrenttemperature(currenttemperature);
        }
        otherExcleModel.setInputdatetime(inputdatetime);
        otherExcleModel.setEquipmentname(equipmentname);
        return otherExcleModel;

    }

    public static PyxExcleModel getPyx(SingleTimeEquipmentModel sing) {
        PyxExcleModel pyxExcleModel = new PyxExcleModel();
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        String equipmentname = sing.getEquipmentname();
        String inputdatetime = sing.getInputdatetime();
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
        pyxExcleModel.setInputdatetime(inputdatetime);
        pyxExcleModel.setEquipmentname(equipmentname);
        return pyxExcleModel;

    }

    public static HjExcleModel getHj(SingleTimeEquipmentModel sing) {
        HjExcleModel hjExcleModel = new HjExcleModel();
        String equipmentname = sing.getEquipmentname();
        String inputdatetime = sing.getInputdatetime();
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
        hjExcleModel.setEquipmentname(equipmentname);
        return hjExcleModel;

    }
}
