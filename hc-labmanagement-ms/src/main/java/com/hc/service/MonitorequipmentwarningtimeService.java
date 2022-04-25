package com.hc.service;


import com.hc.dto.MonitorequipmentwarningtimeDTO;

import java.util.List;

/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface MonitorequipmentwarningtimeService{


    List<MonitorequipmentwarningtimeDTO> selectWarningtimeByHosCode(List<String> hospitalcodes);

    /**
     * 插入报警时段
     * @param monitorequipmentwarningtimeDTO
     */
    void insetWarningtimeList(MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO);



    /**
     * 批量修改报警时间段集合
     * @param updateList
     */
    void updateList(List<MonitorequipmentwarningtimeDTO> updateList);

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
}

