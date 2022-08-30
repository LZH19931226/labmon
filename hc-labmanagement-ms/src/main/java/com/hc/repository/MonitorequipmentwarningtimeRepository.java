package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.po.MonitorEquipmentWarningTimePo;

import java.util.List;


public interface MonitorequipmentwarningtimeRepository extends IService <MonitorEquipmentWarningTimePo>{

    /**
     * 插入报警时段
     * @param monitorequipmentwarningtimeDTO
     */
    void insetWarningtimeList(List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTO);


    /**
     * 删除报警时段
     * @param monitorequipmentwarningtimeDTO
     */
    void deleteInfo(MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO);

    /**
     * 获取报警时段集合
     * @param hospitalCode
     * @param equipmentNo
     * @return
     */
    List<MonitorequipmentwarningtimeDTO> selectWarningtimeByHosCodeAndEno(String hospitalCode, String equipmentNo);

    /**
     * 获取报警时段
     * @param equipmentNo
     * @return
     */
    Integer selectWarningtimeByEno(String equipmentNo);

    /**
     * 获取超时集合
     * @param equipmentNoList
     * @return
     */
    List<MonitorequipmentwarningtimeDTO> selectWarningtimeByEnoList(List<String> equipmentNoList);

}
