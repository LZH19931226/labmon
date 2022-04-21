package com.hc.appliction;

import com.hc.dto.MonitorInstrumentDto;
import com.hc.service.MonitorInstrumentService;
import com.hc.vo.equimenttype.MonitorinstrumentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监控仪器应用
 * @author hc
 */
@Component
public class MonitorInstrumentApplication {

    @Autowired
    private MonitorInstrumentService monitorInstrumentService;

    public List<MonitorinstrumentVo> selectMonitorInstrumentList() {
        List<MonitorInstrumentDto> dtoList= monitorInstrumentService.selectMonitorInstrumentList();
        return null;
    }
}
