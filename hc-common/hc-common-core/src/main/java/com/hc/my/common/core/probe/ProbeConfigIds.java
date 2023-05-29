package com.hc.my.common.core.probe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProbeConfigIds {

    //高低值比较探头
    public static List<Integer> lowHighRuleInstrumentConfigIds = Arrays.asList(1, 2, 4, 5, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 42, 43, 45);
    //市电比较探头
    public static List<Integer> mainsInstrumentConfigIds = Collections.singletonList(10);
    //报警信号比较探头
    public static List<Integer> alarmSignalInstrumentConfigIds = Arrays.asList(11, 44);
    //气体比较探头
    public static List<Integer> gasInstrumentConfigIds = Arrays.asList(3, 6, 7, 8, 9, 12, 26, 27, 28, 35, 23, 24);
    //气流比较探头
    public static List<Integer> airFlowInstrumentConfigIds = Collections.singletonList(25);
}
