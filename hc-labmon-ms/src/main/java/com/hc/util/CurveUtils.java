package com.hc.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hc.application.curvemodel.CurveDataModel;
import com.hc.application.curvemodel.SeriesDataModel;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.ObjectConvertUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class CurveUtils {

    public static CurveInfoDto getCurveFirst(List<Monitorequipmentlastdata> lastDataModelList, List<String> instrumentConfigIdList, Map<String, List<InstrumentParamConfigDto>> map) {
        Map<String, Curve> containerMap = getList(instrumentConfigIdList);
        for (Monitorequipmentlastdata monitorequipmentlastdata : lastDataModelList) {
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(monitorequipmentlastdata);
            filterMap(objectToMap,instrumentConfigIdList);
            for (String field : instrumentConfigIdList) {
                if(!containerMap.containsKey(field) || !objectToMap.containsKey(field) || StringUtils.isBlank((String)objectToMap.get(field))){
                    continue;
                }
                String str =  (String)objectToMap.get(field);
                Curve curve = containerMap.get(field);
                List<String> dataList = curve.getDataList();
                dataList.add(str);
                String timeStr =  (String)objectToMap.get("inputdatetime");
                curve.getDateList().add(DateUtils.getMMdd(timeStr));
            }
        }
        CurveInfoDto curveInfoDto = new CurveInfoDto();
        for (String field : instrumentConfigIdList) {
            if(containerMap.containsKey(field)){
                Curve curve = containerMap.get(field);
                List<String> dataList = curve.getDataList();
                List<String> dateList = curve.getDateList();
                String imField = DataFieldEnum.fromByLastDataField(field).getImField();
                CurveDataModel curveDataModel = generateCurveDataModel(dataList, dateList, map.get(imField));
                setCurveInfo(curveInfoDto,curveDataModel,field);
            }
        }
        return curveInfoDto;
    }

    private static  CurveDataModel generateCurveDataModel(List<String> dataList, List<String> timeList,List<InstrumentParamConfigDto> list){
        CurveDataModel curveDataModel = new CurveDataModel();
        curveDataModel.setXaxis(timeList);
        SeriesDataModel seriesDataModel = new SeriesDataModel();
        seriesDataModel.setDate(dataList);
        if (CollectionUtils.isNotEmpty(list)) {
            curveDataModel.setMaxNum(list.get(0).getHighLimit()+"");
            curveDataModel.setMinNum(list.get(0).getLowLimit()+"");
            curveDataModel.setStyleMin(list.get(0).getStyleMin()+"");
            curveDataModel.setStyleMax(list.get(0).getStyleMax()+"");
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
            switch (field) {
                case "currenttemperature":
                    map.put("currenttemperature",curve);
                    break;
                case "currentcarbondioxide":
                    map.put("currentcarbondioxide",curve);
                    break;
                case "currento2":
                    map.put("currento2",curve);
                    break;
                case "currentairflow":
                    map.put("currentairflow",curve);
                    break;
                case "currenthumidity":
                    map.put("currenthumidity",curve);
                    break;
                case "currentvoc":
                    map.put("currentvoc",curve);
                    break;
                case "currentformaldehyde":
                    map.put("currentformaldehyde",curve);
                    break;
                case "currentpm25":
                    map.put("currentpm25",curve);
                    break;
                case "currentpm10":
                    map.put("currentpm10",curve);
                    break;
                case "currentqc":
                    map.put("currentqc",curve);
                    break;
                case "currentairflow1":
                    map.put("currentairflow1",curve);
                    break;
                case "currenttemperature1":
                    map.put("currenttemperature1",curve);
                    break;
                case "currenttemperature2":
                    map.put("currenttemperature2",curve);
                    break;
                case "currenttemperature3":
                    map.put("currenttemperature3",curve);
                    break;
                case "currenttemperature4":
                    map.put("currenttemperature4",curve);
                    break;
                case "currenttemperature5":
                    map.put("currenttemperature5",curve);
                    break;
                case "currenttemperature6":
                    map.put("currenttemperature6",curve);
                    break;
                case "currenttemperature7":
                    map.put("currenttemperature7",curve);
                    break;
                case "currenttemperature8":
                    map.put("currenttemperature8",curve);
                    break;
                case "currenttemperature9":
                    map.put("currenttemperature9",curve);
                    break;
                case "currenttemperature10":
                    map.put("currenttemperature10",curve);
                    break;
                case "currentlefttemperature":
                    map.put("currentlefttemperature",curve);
                    break;
                case "currentrigthtemperature":
                    map.put("currentrigthtemperature",curve);
                    break;
                case "currentpm5":
                    map.put("currentpm5",curve);
                    break;
                case "currentpm05":
                    map.put("currentpm05",curve);
                    break;
                case "currentleftcovertemperature":
                    map.put("currentleftcovertemperature",curve);
                    break;
                case "currentleftendtemperature":
                    map.put("currentleftendtemperature",curve);
                    break;
                case "currentleftairflow":
                    map.put("currentleftairflow",curve);
                    break;
                case "currentrightcovertemperature":
                    map.put("currentrightcovertemperature",curve);
                    break;
                case "currentrightendtemperature":
                    map.put("currentrightendtemperature",curve);
                    break;
                case "currentrightairflow":
                    map.put("currentrightairflow",curve);
                    break;
                case "currentn2":
                    map.put("currentn2",curve);
                    break;
                case "leftCompartmentHumidity":
                    map.put("leftCompartmentHumidity",curve);
                    break;
                case "rightCompartmentHumidity":
                    map.put("rightCompartmentHumidity",curve);
                    break;
                case "liquidLevel":
                    map.put("liquidLevel",curve);
                    break;
                default:
                    break;
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
