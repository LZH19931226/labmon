package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.po.MonitorEquipmentWarningTimePo;


public interface MonitorequipmentwarningtimeRepository extends IService <MonitorEquipmentWarningTimePo>{

    /**
     * 插入报警时段
     * @param monitorequipmentwarningtimeDTO
     */
    void insetWarningtimeList(MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO);
}