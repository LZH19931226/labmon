package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.vo.equimenttype.MonitorEquipmentVo;

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
    List<MonitorEquipmentDto> getEquipmentInfoList(Page<MonitorEquipmentVo> page, MonitorEquipmentCommand monitorEquipmentCommand);

    /**
     * 插入监控设备信息
     * @param monitorEquipmentDto
     */
    void insertMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto);

    /**
     * 更新监控设备信息
     * @param monitorEquipmentDto
     */
    void updateMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto);

    /**
     * 查询监控设备个数
     * @param monitorEquipmentDto
     * @return
     */
    Integer selectCount(MonitorEquipmentDto monitorEquipmentDto);

    /**
     *
     * @param equipmentNo
     * @return
     */
    MonitorEquipmentDto selectMonitorEquipmentInfoByNo(String equipmentNo);
}
