package com.hc.application.response;

import lombok.Data;

import java.util.List;

@Data
public class PointInTimeDataTableResult {

    private String date;

    private List<TableResult> list ;
}
