package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.po.MonitorEquipmentPo;

import java.util.List;

/**
 *
 * @author hc
 */
public interface MonitorEquipmentRepository extends IService<MonitorEquipmentPo> {

    /**
     * 分页查询监控设备信息
     *
     * @param page                    分页对象
     * @param monitorEquipmentCommand 监控设备参数
     * @return
     */
    List<MonitorEquipmentDto> getEquipmentInfoList(Page page, MonitorEquipmentCommand monitorEquipmentCommand);

    /**
     * 插入监控设备信息
     * @param monitorEquipmentDto
     */
    void insertMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto);

    /**
     * 更新井控设备信息
     * @param monitorEquipmentDto
     */
    void updateMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto);
}
