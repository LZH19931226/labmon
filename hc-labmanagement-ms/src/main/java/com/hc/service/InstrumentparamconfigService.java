package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentConfigDTO;
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


    List<InstrumentConfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo);

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

    /**
     * 查询所有的信息
     * @return
     */
    List<InstrumentparamconfigDTO> selectInstrumentparamconfigAllInfo();

    /**
     * 更新最新一次的报警时间
     * @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime 报警时间
     */
    void editWarningTime(String instrumentParamConfigNo, String warningTime);

    /**
     * 查询状态为1的数量
     * @param instrumentNo
     * @return
     */
    int selectProbeStateCount(String instrumentNo);

    /**
     * 获取设备探头信息
     * @param equipmentNo
     * @return
     */
    List<InstrumentparamconfigDTO> getInstrumentParamConfigInfo(String equipmentNo);

    /**
     * 批量插入探头信息
     * @param probeList
     */
    void insertBatch(List<InstrumentparamconfigDTO> probeList);

    /**
     * 批量更新探头信息
     * @param list
     */
    void updateBatch(List<InstrumentparamconfigDTO> list);

    /**
     * 获取设备已添加探头监测类型
     * @param equipmentNo
     * @return
     */
    List<String> getEquipmentAddProbeInfo(String equipmentNo);

    void updateProbeAlarmState(String instrumentParamConfigNo, String warningPhone);

    void batchUpdateProbeAlarmState(String warningPhone, String equipmentNo);

    List<InstrumentparamconfigDTO> getInstrumentParamConfigByCodeAndTypeId(String hospitalCode, String equipmentTypeId);

    void batchProbeAlarmState(List<String> probeIds, String warningPhone);

    /**
     * 查出所有的探头信息
     * @return
     */
    List<InstrumentparamconfigDTO> list();
}

