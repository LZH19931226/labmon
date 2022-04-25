package com.hc.service;


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
public interface MonitorinstrumenttypeService{


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
    List<MonitorinstrumenttypeDTO> seleclAll();
}

