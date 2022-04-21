package com.hc.controller;

import com.hc.appliction.MonitorInstrumentApplication;
import com.hc.vo.equimenttype.MonitorinstrumentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 监控仪器控制器
 * @author hc
 */
@RestController
@RequestMapping("/MonitorInstrument")
public class MonitorInstrumentController {
    @Autowired
    private MonitorInstrumentApplication monitorInstrumentApplication;

    @GetMapping("/findMonitorInstrumentList")
    public List<MonitorinstrumentVo> getMonitorInstrumentList(){
       return monitorInstrumentApplication.selectMonitorInstrumentList();
    }
}
