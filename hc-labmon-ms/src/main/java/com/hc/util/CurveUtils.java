package com.hc.util;

import com.hc.application.curvemodel.CurveDataModel;
import com.hc.application.curvemodel.SeriesDataModel;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.ObjectConvertUtils;
import com.hc.my.common.core.util.RegularUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CurveUtils {

    public static List<Map<String,CurveDataModel>> getCurveFirst(List<Monitorequipmentlastdata> lastDataModelList, List<String> instrumentConfigIdList, Map<String, List<InstrumentParamConfigDto>> map) {
        Map<String, Curve> containerMap = getList(instrumentConfigIdList);
        for (Monitorequipmentlastdata monitorequipmentlastdata : lastDataModelList) {
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(monitorequipmentlastdata);
            filterMap(objectToMap,instrumentConfigIdList);
            for (String field : instrumentConfigIdList) {
                //去除不需要展示的数据，去除对象中没有的字段，去除字段值为空的数据，去除数据不含数字的数据
                if(!containerMap.containsKey(field) || !objectToMap.containsKey(field) || StringUtils.isBlank((String)objectToMap.get(field)) || !RegularUtil.checkContainsNumbers((String)objectToMap.get(field))){
                    continue;
                }
                String str =  (String)objectToMap.get(field);
                Curve curve = containerMap.get(field);
                List<String> dataList = curve.getDataList();
                dataList.add(str);
                String timeStr =  (String)objectToMap.get("inputdatetime");
                curve.getDateList().add(DateUtils.getHHmm(timeStr));
            }
        }
        List<Map<String,CurveDataModel>> list = new ArrayList<>();
        for (String field : instrumentConfigIdList) {
            if(containerMap.containsKey(field)){
                Map<String,CurveDataModel> resultMap = new HashMap<>();
                Curve curve = containerMap.get(field);
                List<String> dataList = curve.getDataList();
                List<String> dateList = curve.getDateList();
                String imField = DataFieldEnum.fromByLastDataField(field).getImField();
                CurveDataModel curveDataModel = generateCurveDataModel(dataList, dateList, map.get(imField));
                resultMap.put(field,curveDataModel);
                list.add(resultMap);
            }
        }
        return list;
    }

    private static  CurveDataModel generateCurveDataModel(List<String> dataList, List<String> timeList,List<InstrumentParamConfigDto> list){
        CurveDataModel curveDataModel = new CurveDataModel();
        curveDataModel.setXaxis(timeList);
        SeriesDataModel seriesDataModel = new SeriesDataModel();
        seriesDataModel.setDate(dataList);
        //数据库不为空时去数据库中的值
        if (CollectionUtils.isNotEmpty(list)) {
            InstrumentParamConfigDto probe = list.get(0);
            curveDataModel.setMaxNum(probe.getHighLimit()+"");
            curveDataModel.setMinNum(probe.getLowLimit()+"");
            curveDataModel.setStyleMin(StringUtils.isBlank(probe.getStyleMin()) ? probe.getHighLimit()+"":probe.getStyleMin());
            curveDataModel.setStyleMax(StringUtils.isBlank(probe.getStyleMax()) ? probe.getLowLimit()+"":probe.getStyleMax());
        }else {
            OptionalDouble max = dataList.stream().mapToDouble(Double::parseDouble).max();
            if (max.isPresent()) {
                curveDataModel.setMaxNum(max.getAsDouble()+"");
                curveDataModel.setStyleMax(max.getAsDouble()+"");
            }else {
                curveDataModel.setMaxNum("");
                curveDataModel.setStyleMax("");
            }
            OptionalDouble min = dataList.stream().mapToDouble(Double::parseDouble).min();
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

    private static void filterMap(Map<String, Object> objectMap,List<String> list) {
        list.add("inputdatetime");
        Iterator<String> iterator = objectMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if(!list.contains(next)){
                iterator.remove();
                objectMap.remove(next);
            }
        }
        list.remove("inputdatetime");
    }

    public static Map<String,Curve> getList(List<String> list){
        Map<String,Curve> map = new HashMap<>();
        for (String field : list) {
            Curve curve = new Curve();
            if(!StringUtils.equalsAnyIgnoreCase(field,"voltage","currentdoorstate2","currentdoorstate","currentups","currentqcl","qccurrent","power")){
                map.put(field,curve);
            }
        }
        return map;
    }

    /**
     * 设置曲线信息
     * @param curveInfoDto
     * @param curveDataModel
     * @param field
     */
    private static void setCurveInfo(CurveInfoDto curveInfoDto, CurveDataModel curveDataModel, String field) {
        switch (field){
            case "currento2":
                curveInfoDto.setO2(curveDataModel);
                break;
            case "currenttemperature":
                curveInfoDto.setTemp(curveDataModel);
                break;
            case "currentcarbondioxide":
                curveInfoDto.setCo2(curveDataModel);
                break;
            case "currentvoc":
                curveInfoDto.setVoc(curveDataModel);
                break;
            case "currenthumidity":
                curveInfoDto.setRh(curveDataModel);
                break;
            case "currentairflow":
                curveInfoDto.setPress(curveDataModel);
                break;
            case "currentqc":
                curveInfoDto.setQc(curveDataModel);
                break;
            case "currentpm25":
                curveInfoDto.setPm25(curveDataModel);
                break;
            case "currentpm10":
                curveInfoDto.setPm10(curveDataModel);
                break;
            case "currentformaldehyde":
                curveInfoDto.setJq(curveDataModel);
                break;
            case "currenttemperature1":
                curveInfoDto.setTemp1(curveDataModel);
                break;
            case "currenttemperature2":
                curveInfoDto.setTemp2(curveDataModel);
                break;
            case "currenttemperature3":
                curveInfoDto.setTemp3(curveDataModel);
                break;
            case "currenttemperature4":
                curveInfoDto.setTemp4(curveDataModel);
                break;
            case "currenttemperature5":
                curveInfoDto.setTemp5(curveDataModel);
                break;
            case "currenttemperature6":
                curveInfoDto.setTemp6(curveDataModel);
                break;
            case "currenttemperature7":
                curveInfoDto.setTemp7(curveDataModel);
                break;
            case "currenttemperature8":
                curveInfoDto.setTemp8(curveDataModel);
                break;
            case "currenttemperature9":
                curveInfoDto.setTemp9(curveDataModel);
                break;
            case "currenttemperature10":
                curveInfoDto.setTemp10(curveDataModel);
                break;
            case "currentlefttemperature":
                curveInfoDto.setLefttemp(curveDataModel);
                break;
            case "currentrigthtemperature":
                curveInfoDto.setRighttemp(curveDataModel);
                break;
            case "currentairflow1":
                curveInfoDto.setAirflow(curveDataModel);
                break;
            case "PM5":
                curveInfoDto.setPm5(curveDataModel);
                break;
            case "PM05":
                curveInfoDto.setPm05(curveDataModel);
                break;
            case "currentleftcovertemperature":
                curveInfoDto.setLeftcovertemp(curveDataModel);
                break;
            case "currentleftendtemperature":
                curveInfoDto.setLeftendtemp(curveDataModel);
                break;
            case "currentleftairflow":
                curveInfoDto.setLeftair(curveDataModel);
                break;
            case "currentrightcovertemperature":
                curveInfoDto.setRightcovertemp(curveDataModel);
                break;
            case "currentrightendtemperature":
                curveInfoDto.setRightendtemp(curveDataModel);
                break;
            case "currentrightairflow":
                curveInfoDto.setRightair(curveDataModel);
                break;
            case "currentn2":
                curveInfoDto.setN2(curveDataModel);
                break;
            case "leftCompartmentHumidity":
                curveInfoDto.setLeftCompartmentHumidity(curveDataModel);
                break;
            case "rightCompartmentHumidity":
                curveInfoDto.setRightCompartmentHumidity(curveDataModel);
                break;
            default:
                break;
        }
    }
    
    
    @Data
    @NoArgsConstructor
    public static class Curve{

        private List<String> dataList = new ArrayList<>();

        private List<String> dateList = new ArrayList<>();
    }
}
