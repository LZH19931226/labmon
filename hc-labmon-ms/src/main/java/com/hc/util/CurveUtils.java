package com.hc.util;

import com.hc.application.curvemodel.CurveDataModel;
import com.hc.application.curvemodel.SeriesDataModel;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.dto.CurveInfoDto;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.my.common.core.constant.enums.DataFieldEnum;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.Mt310DCUtils;
import com.hc.my.common.core.util.ObjectConvertUtils;
import com.hc.my.common.core.util.RegularUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CurveUtils {

    public static List<Map<String,CurveDataModel>> getCurveFirst(List<Monitorequipmentlastdata> lastDataModelList, List<String> lastDataFieldList, Map<String, List<InstrumentParamConfigDto>> map,String eqSnAbbreviation) {
        //将传入的list保存到新集合
        List<String> dcFields = new ArrayList<>(lastDataFieldList);
        //修改设备过滤字段集合
        editLastFieldList(lastDataFieldList,eqSnAbbreviation);
        //根据前端传入的字段创建需要的容器
        Map<String, Curve> containerMap = getList(dcFields);
        //遍历数据
        for (Monitorequipmentlastdata monitorequipmentlastdata : lastDataModelList) {
            //将对象转map并过滤
            Map<String, Object> objectToMap = ObjectConvertUtils.getObjectToMap(monitorequipmentlastdata);
            ObjectConvertUtils.filterMap(objectToMap,lastDataFieldList);
            //给创建的容器赋值
            containerMapAssignment(containerMap,objectToMap,dcFields,eqSnAbbreviation);
        }
        //以固定格式返回
        List<Map<String,CurveDataModel>> list = new ArrayList<>();
        for (String field : dcFields) {
            if(null != containerMap.get(field)){
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

    /**
     * 通过设备sn简称给容器map赋值
     */
    private static void containerMapAssignment(Map<String, Curve> containerMap, Map<String, Object> objectToMap, List<String> lastDataFieldList,String eqSnAbbreviation) {
        //特殊设备MT310DC
        if (SysConstants.MT310_SN.equals(eqSnAbbreviation)) {
            setMT310DC(containerMap, objectToMap, lastDataFieldList);
            return;
        }
        setDefault(containerMap, objectToMap, lastDataFieldList);
    }

    private static void setDefault(Map<String, Curve> containerMap, Map<String, Object> objectToMap, List<String> lastDataFieldList) {
        for (String field : lastDataFieldList) {
            if(!conditionCheck2(containerMap,objectToMap,field)){
                defaultSetValue(containerMap,objectToMap,field);
            }
        }
    }

    private static void setMT310DC(Map<String, Curve> containerMap, Map<String, Object> objectToMap, List<String> lastDataFieldList) {
        for (String field : lastDataFieldList) {
            switch (field){
                case SysConstants.MT310DC_DATA_CO2:
                case SysConstants.MT310DC_DATA_O2:
                case SysConstants.MT310DC_DATA_VOC:
                    if(!conditionCheck(containerMap,objectToMap,field)){
                        defaultSetValue(containerMap,objectToMap,field);
                    }
                    break;
                case SysConstants.MT310DC_DATA_TEMP:
                    MT310DCSetValue(containerMap,objectToMap,field,SysConstants.MT310DC_TEMP);
                    break;
                case SysConstants.MT310DC_DATA_RH:
                    MT310DCSetValue(containerMap,objectToMap,field,SysConstants.MT310DC_RH);
                    break;
                case SysConstants.MT310DC_DATA_OUTER_O2:
                    MT310DCSetValue(containerMap,objectToMap,field,SysConstants.MT310DC_O2);
                    break;
                case SysConstants.MT310DC_DATA_OUTER_CO2:
                    MT310DCSetValue(containerMap,objectToMap,field,SysConstants.MT310DC_CO2);
                    break;
            }
        }
    }

    /**
     *判断该数据是否有有效数据
     */
    private static boolean conditionCheck2(Map<String, Curve> containerMap, Map<String, Object> objectToMap, String field) {
        //去除不需要展示的数据，去除对象中没有的字段，去除字段值为空的数据，去除数据不含数字的数据
        return conditionCheck(containerMap,objectToMap,field) || !objectToMap.containsKey(field);
    }

    /**
     *判断该数据是否有有效数据
     */
    private static boolean conditionCheck(Map<String, Curve> containerMap, Map<String, Object> objectToMap, String field) {
        //去除不需要展示的数据，去除字段值为空的数据，去除数据不含数字的数据
        return !containerMap.containsKey(field)  || StringUtils.isBlank((String)objectToMap.get(field)) || !RegularUtil.checkContainsNumbers((String)objectToMap.get(field));
    }

    private static void MT310DCSetValue(Map<String, Curve> containerMap, Map<String, Object> objectToMap, String field,String model) {
        String data = null;
        if(model.equals((String)objectToMap.get("probe1model"))){
            data = (String)objectToMap.get("probe1data");
        }
        if(model.equals((String)objectToMap.get("probe2model"))){
            data = (String)objectToMap.get("probe2data");
        }
        if(model.equals((String)objectToMap.get("probe3model"))){
            data = (String)objectToMap.get("probe3data");
        }
        Curve curve = containerMap.get(field);
        List<String> dataList = curve.getDataList();
        //当data不是空并且为数字时设置值
        if(StringUtils.isNotBlank(data) && RegularUtil.checkContainsNumbers(data)){
            dataList.add(data);
            String timeStr =  (String)objectToMap.get(SysConstants.INPUT_DATETIME);
            curve.getDateList().add(DateUtils.getHHmm(timeStr));
        }
    }

    private static void defaultSetValue(Map<String, Curve> containerMap, Map<String, Object> objectToMap, String field) {
        String str =  (String)objectToMap.get(field);
        Curve curve = containerMap.get(field);
        List<String> dataList = curve.getDataList();
        dataList.add(str);
        String timeStr =  (String)objectToMap.get(SysConstants.INPUT_DATETIME);
        curve.getDateList().add(DateUtils.getHHmm(timeStr));
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

    //MT310DC过滤方法
    private static void editLastFieldList(List<String> list,String eqSnAbbreviation){
        if(Mt310DCUtils.isMT310DC(eqSnAbbreviation)){
            Mt310DCUtils.get310DCList(list);
        }
    }

    public static Map<String,Curve> getList(List<String> list){
        Map<String,Curve> map = new HashMap<>();
        for (String field : list) {
            Curve curve = new Curve();
            //去除不需要创建容器的字段
            if(!StringUtils.equalsAnyIgnoreCase(field,DataFieldEnum.voltage.getLastDataField(),DataFieldEnum.QC.getLastDataField(),
                    DataFieldEnum.DOOR2.getLastDataField(),DataFieldEnum.DOOR.getLastDataField(),DataFieldEnum.UPS.getLastDataField(),
                    DataFieldEnum.QCL.getLastDataField(),DataFieldEnum.CURRENT.getLastDataField(),DataFieldEnum.power.getLastDataField())){
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
