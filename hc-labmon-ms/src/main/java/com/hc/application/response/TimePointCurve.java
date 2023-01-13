package com.hc.application.response;

import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TimePointCurve {

    public  List<String> xaxis;

    public String name;

    public List<String> series;


}
