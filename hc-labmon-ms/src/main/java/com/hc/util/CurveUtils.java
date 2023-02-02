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
import com.hc.my.common.core.util.RegularUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

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
            curveDataModel.setStyleMin(StringUtils.isBlank(probe.getStyleMin()) ? "":probe.getStyleMin());
            curveDataModel.setStyleMax(StringUtils.isBlank(probe.getStyleMax()) ? "":probe.getStyleMax());
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
            map.put(field,curve);
        }
        return map;
    }
    
    
    @Data
    @NoArgsConstructor
    public static class Curve{

        private List<String> dataList = new ArrayList<>();

        private List<String> dateList = new ArrayList<>();
    }
}
