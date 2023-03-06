package com.hc.my.common.core.util;

import com.hc.my.common.core.constant.enums.ProbeOutlierMt310;
import com.hc.my.common.core.constant.enums.SysConstants;

import java.util.Arrays;
import java.util.List;

public class Mt310DCUtils {

    public static void get310DCList(List<String > list){
        //当字段中存在外置探头时添加model1,data1,model2,data2,model3,data3字段
        //外置探头有：outerCO2,outerO2,currenttemperature,currenthumidity
        List<String> mt310dc = Arrays.asList(SysConstants.MT310DC_DATA_OUTER_O2, SysConstants.MT310DC_DATA_OUTER_CO2, SysConstants.MT310DC_DATA_TEMP, SysConstants.MT310DC_DATA_RH);
        long count = list.stream().filter(mt310dc::contains).count();
        if(count>0){
            list.addAll(Arrays.asList("probe1model","probe1data","probe2model","probe2data","probe3model","probe3data"));
        }
    }

    public static List<String> get310DCFields(List<String > list){
        get310DCList(list);
        list.remove(SysConstants.MT310DC_DATA_OUTER_O2);
        list.remove(SysConstants.MT310DC_DATA_OUTER_CO2);
        return list;
    }

    /**
     * 通过sn5到6判断设备是否是MT310DC
     */
    public static boolean isMT310DC(String eqSnAbbreviation){
        return ProbeOutlierMt310.THREE_ONE.getCode().equals(eqSnAbbreviation);
    }
}
