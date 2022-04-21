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
}

