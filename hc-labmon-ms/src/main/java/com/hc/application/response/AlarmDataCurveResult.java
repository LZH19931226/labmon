package com.hc.application.response;

import lombok.Data;

import java.util.List;

@Data
public class AlarmDataCurveResult {

    public List<String> equipmentNameList;

    public List<Long> numList;
}
