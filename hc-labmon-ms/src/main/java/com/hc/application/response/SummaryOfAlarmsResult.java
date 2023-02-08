package com.hc.application.response;

import lombok.Data;

import java.util.List;

@Data
public class SummaryOfAlarmsResult {

    private List<String> timeList;

    private List<String> numList;
}
