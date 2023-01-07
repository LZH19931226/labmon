package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentConfigCommand;
import com.hc.dto.InstrumentConfigDTO;

import java.util.List;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface InstrumentConfigService {

    /**
     * 查询探头配置信息
     * @param instrumentconfigid
     * @return
     */
    InstrumentConfigDTO selectInfoByConfigid(Integer instrumentconfigid);

    /**
     * 查询所有的探头配置信息
     * @return
     */
    List<InstrumentConfigDTO> selectAllInfo();

    void save(InstrumentConfigCommand instrumentConfigCommand);

    /**
     * 分页查询监控参数类型信息
     * @param page
     * @param instrumentConfigName
     * @return
     */
    List<InstrumentConfigDTO> listByPage(Page<InstrumentConfigDTO> page, String instrumentConfigName);

    void edit(InstrumentConfigCommand instrumentConfigCommand);

    void remove(String instrumentConfigId);

    List<InstrumentConfigDTO> list();
}

