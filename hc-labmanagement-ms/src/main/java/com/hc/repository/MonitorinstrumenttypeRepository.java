package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.po.MonitorinstrumenttypePo;

import java.util.List;


public interface MonitorinstrumenttypeRepository extends IService <MonitorinstrumenttypePo>{


    /**
     * 查询监控设备类型信息
     * @param
     * @return
     */
    MonitorequipmenttypeDTO selectinfoByTypeid(Integer instrumenttypeid);

    /**
     * 查询所有的监控设备类型信息
     * @return
     */
    List<MonitorinstrumenttypeDTO> seleclAll();

    List<MonitorinstrumenttypeDTO> listByPage(Page<MonitorinstrumenttypeDTO> page, String instrumentTypeName);
}
