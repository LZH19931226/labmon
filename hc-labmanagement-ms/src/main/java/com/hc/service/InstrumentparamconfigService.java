package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;

import java.util.List;

/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface InstrumentparamconfigService{


    /**
     * 插入探头配置信息
     * @param instrumentparamconfigDTO
     */
    void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO);

    /**
     * 仪器配置参数集合
     * @param instrumentNos
     * @return
     */
    List<InstrumentparamconfigDTO> slectInfo(List<String> instrumentNos);

    /**
     * 删除探头参数信息
     * @param instrumentno 仪器id
     */
    void deleteInfoByEno(String instrumentno);


    List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo);

    /**
     * 更新探头配置信息
     * @param instrumentparamconfigDTO
     */
    void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO);

    /**
     * 删除探头头信息
     * @param setInstrumentparamconfigno
     */
    void deleteInfo(InstrumentparamconfigDTO setInstrumentparamconfigno);

    /**
     * 分页获取探头信息
     * @param page
     * @param instrumentparamconfigCommand
     * @return
     */
    List<InstrumentparamconfigDTO> findInstrumentparamconfig(Page<InstrumentparamconfigVo> page, InstrumentparamconfigCommand instrumentparamconfigCommand);

    /**
     * 查询探头信息是否已存在
     * @param instrumentNo
     * @param instrumentConfigId
     * @param instrumentTypeId
     * @return
     */
    Integer selectCount(String instrumentNo, Integer instrumentConfigId, Integer instrumentTypeId);

    /**
     * 批量删除探头信息
     * @param instrumentParamConfigNo
     */
    void deleteInfos(String[] instrumentParamConfigNo);

    /**
     * 通过探头no查询探头信息
     * @param instrumentparamconfigno
     * @return
     */
    InstrumentparamconfigDTO selectInstrumentparamconfigInfo(String instrumentparamconfigno);
}

