package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.po.InstrumentparamconfigPo;

import java.util.List;


public interface InstrumentparamconfigRepository extends IService <InstrumentparamconfigPo>{

    /**
     * 插入探头配置信息
     * @param instrumentparamconfigDTO
     */
    void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO);

    /**
     * 获取仪器信息集合
     * @param instrumentNos
     * @return
     */
    List<InstrumentparamconfigDTO> slectinfo(List<String> instrumentNos);

    /**
     * 删除探头参数信息
     * @param instrumentno
     */
    void deleteInfoByEno(String instrumentno);


    List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo);

    /**
     * 更新探头配置信息
     * @param instrumentparamconfigDTO
     */
    void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO);

    /**
     * 删除探头信息
     * @param instrumentparamconfigno
     */
    void deleteInstrumentparamconfig(InstrumentparamconfigDTO instrumentparamconfigno);

    /**
     * 分页获取探头信息
     * @param page
     * @param instrumentparamconfigCommand
     * @return
     */
    List<InstrumentparamconfigDTO> findInstrumentparamconfig(Page page, InstrumentparamconfigCommand instrumentparamconfigCommand);

    /**
     * 查询探头信息是否已存在
     * @param instrumentNo
     * @param instrumentConfigId
     * @param instrumentTypeId
     * @return
     */
    Integer selectCount(String instrumentNo, Integer instrumentConfigId, Integer instrumentTypeId);
}