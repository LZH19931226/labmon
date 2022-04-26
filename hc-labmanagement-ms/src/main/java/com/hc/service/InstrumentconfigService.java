package com.hc.service;


import com.hc.dto.InstrumentconfigDTO;

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
}

