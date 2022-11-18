package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorInstrumentTypeCommand;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.service.InstrumentmonitorService;
import com.hc.service.MonitorinstrumenttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class MonitorInstrumentTypeApplication {

    @Autowired
    private MonitorinstrumenttypeService monitorInstrumentTypeService;

    @Autowired
    private InstrumentmonitorService instrumentMonitorService;

    /**
     * 添加设备
     * @param monitorInstrumentTypeCommand
     */
    public void add(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand) {
        monitorInstrumentTypeService.add(monitorInstrumentTypeCommand);
    }

    /**
     * 分页查询
     * @param monitorInstrumentTypeCommand
     * @return
     */
    public Page<MonitorinstrumenttypeDTO> list(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand) {
        Integer pageCurrent = monitorInstrumentTypeCommand.getPageCurrent();
        Integer pageSize = monitorInstrumentTypeCommand.getPageSize();
        String instrumentTypeName = monitorInstrumentTypeCommand.getInstrumentTypeName();
        Page<MonitorinstrumenttypeDTO> page = new Page<>(pageCurrent,pageSize);
        List<MonitorinstrumenttypeDTO> list = monitorInstrumentTypeService.listByPage(page,instrumentTypeName);
        page.setRecords(list);
        return page;
    }

    /**
     * 修改
     * @param monitorInstrumentTypeCommand
     */
    public void edit(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand) {
        monitorInstrumentTypeService.edit(monitorInstrumentTypeCommand);
    }

    /**
     * 删除
     * @param instrumentTypeId
     */
    public void remove(String instrumentTypeId) {
        int  count = instrumentMonitorService.countByInstrumentTypeId(instrumentTypeId);
        if(count>0){
            throw  new RuntimeException(LabSystemEnum.DEVICES_EXIST_UNDER_THIS_DEVICE_TYPE.getMessage());
        }
        monitorInstrumentTypeService.remove(instrumentTypeId);
    }
}
