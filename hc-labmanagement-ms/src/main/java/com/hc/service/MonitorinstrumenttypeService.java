package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorInstrumentTypeCommand;
import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;

import java.util.List;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface MonitorinstrumenttypeService {


    /**
     * 查询监控设备类型信息
     * @param instrumenttypeid
     * @return
     */
    MonitorequipmenttypeDTO selectinfo(Integer instrumenttypeid);

    /**
     * 查询所有的监控设备类型信息
     * @return
     */
    List<MonitorinstrumenttypeDTO> selectAll();

    /**
     * 添加设备
     * @param monitorInstrumentTypeCommand
     */
    void add(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand);

    /**
     * 分页获取监控设备类型信息
     * @param page
     * @param instrumentTypeName
     * @return
     */
    List<MonitorinstrumenttypeDTO> listByPage(Page<MonitorinstrumenttypeDTO> page, String instrumentTypeName);

    /**
     * 修改监控设备类型信息
     * @param monitorInstrumentTypeCommand
     */
    void edit(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand);

    /**
     * 删除
     * @param instrumentTypeId
     */
    void remove(String instrumentTypeId);
}

