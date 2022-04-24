package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;

import java.util.List;

/**
 * 监控设备服务
 * @author hc
 */
public interface MonitorEquipmentService {

    /**
     * 分页查询监控设备信息
     * @param page 分页对象
     * @param monitorEquipmentCommand 监控设备参数
     * @return
     */
    List<MonitorEquipmentDto> getEquipmentInfoList(Page page, MonitorEquipmentCommand monitorEquipmentCommand);

    /**
     * 插入监控设备信息
     * @param monitorEquipmentDto
     */
    void insertMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto);
}
