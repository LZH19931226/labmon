package com.hc.service;


import com.hc.dto.InstrumentconfigDTO;

import java.util.List;

/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface InstrumentconfigService{

    /**
     * 查询探头配置信息
     * @param instrumentconfigid
     * @return
     */
    InstrumentconfigDTO selectInfoByConfigid(Integer instrumentconfigid);

    /**
     * 查询所有的探头配置信息
     * @return
     */
    List<InstrumentconfigDTO> selectAllInfo();

}

