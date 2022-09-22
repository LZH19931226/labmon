package com.hc.controller;

import com.hc.appliction.InstrumentMonitorApplication;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@RestController
@Api(tags = "")
@RequestMapping("/instrumentMonitor")
public class InstrumentMonitorController{

    @Autowired
    private InstrumentMonitorApplication instrumentMonitorApplication;

}
