package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.po.InstrumentparamconfigPo;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;

import java.util.Date;
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


    List<InstrumentConfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo);

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
     * @param instrumentParamConfigNos
     */
    void deleteInfos(String[] instrumentParamConfigNos);

    /**
     * 通过探头no查询探头信息
     * @param instrumentparamconfigno
     * @return
     */
    InstrumentparamconfigDTO selectInstrumentparamconfigInfo(String instrumentparamconfigno);

    /**
     * 查询探头参数配置信息
     * @return
     */
    List<InstrumentparamconfigDTO> selectInstrumentparamconfigAllInfo();

    /**
     * 更新最新一次的报警时间
     * @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime 报警时间
     */
    void editWarningTime(String instrumentParamConfigNo, Date warningTime);

    List<InstrumentparamconfigDTO> getInstrumentParamConfigInfo(String equipmentNo);

    void insertBatch(List<InstrumentparamconfigDTO> probeList);

    List<String> getEquipmentAddProbeInfo(String equipmentNo);

    void batchUpdateProbeAlarmState(String warningPhone, String equipmentNo);

    List<InstrumentparamconfigDTO> getInstrumentParamConfigByCodeAndTypeId(String hospitalCode, String equipmentTypeId);

    void batchProbeAlarmState(List<String> probeIds, String warningPhone);

    void updateBatchData(List<InstrumentparamconfigDTO> probeList);
}
