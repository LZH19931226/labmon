package com.hc.controller;

import com.hc.application.StatisticalAnalysisApplication;
import com.hc.application.response.StatisticalResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/as")
@RestController
public class StatisticalAnalysisController {

    @Autowired
    private StatisticalAnalysisApplication statisticalAnalysisApplication;

    /**
     *
     * @return
     */
    @GetMapping("/getStatisticalData")
    public Map<String, Map<String,Long>> getStatisticalData(@RequestParam("time") String time){
        return statisticalAnalysisApplication.getStatisticalData(time);
    }
}
