package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentparamconfigCommand;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.InstrumentparamconfigPo;
import com.hc.repository.InstrumentparamconfigRepository;
import com.hc.service.InstrumentparamconfigService;
import com.hc.vo.equimenttype.InstrumentparamconfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class InstrumentparamconfigServiceImpl implements InstrumentparamconfigService {

    @Autowired
    private InstrumentparamconfigRepository instrumentparamconfigRepository;
    /**
     * 插入探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        instrumentparamconfigRepository.insertInstrumentmonitor(instrumentparamconfigDTO);
    }

    /**
     * 仪器配置参数集合
     *
     * @param instrumentNos
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> slectInfo(List<String> instrumentNos) {
        return instrumentparamconfigRepository.slectinfo(instrumentNos);
    }

    /**
     * 删除探头参数信息
     *
     * @param instrumentno
     */
    @Override
    public void deleteInfoByEno(String instrumentno) {
        instrumentparamconfigRepository.deleteInfoByEno(instrumentno);
    }

    @Override
    public List<InstrumentConfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo) {
        return instrumentparamconfigRepository.selectInstrumentparamconfigByEqNo(equipmentNo);
    }

    /**
     * 更新探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        instrumentparamconfigRepository.updateInfo(instrumentparamconfigDTO);
    }

    /**
     * 删除探头头信息
     *
     * @param instrumentparamconfigno
     */
    @Override
    public void deleteInfo(InstrumentparamconfigDTO instrumentparamconfigno) {
        instrumentparamconfigRepository.deleteInstrumentparamconfig(instrumentparamconfigno);
    }

    /**
     * 分页获取探头信息
     *
     * @param page
     * @param instrumentparamconfigCommand
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> findInstrumentparamconfig(Page<InstrumentparamconfigVo> page, InstrumentparamconfigCommand instrumentparamconfigCommand) {
        return instrumentparamconfigRepository.findInstrumentparamconfig(page,instrumentparamconfigCommand);
    }

    /**
     * 查询探头信息是否已存在
     *
     * @param instrumentNo
     * @param instrumentConfigId
     * @param instrumentTypeId
     * @return
     */
    @Override
    public Integer selectCount(String instrumentNo, Integer instrumentConfigId, Integer instrumentTypeId) {
        return instrumentparamconfigRepository.selectCount(instrumentNo,instrumentConfigId,instrumentTypeId);
    }

    /**
     * 批量删除探头信息
     *
     * @param instrumentParamConfigNos
     */
    @Override
    public void deleteInfos(String[] instrumentParamConfigNos) {
        instrumentparamconfigRepository.deleteInfos(instrumentParamConfigNos);
    }

    /**
     * 通过探头no查询探头信息
     *
     * @param instrumentparamconfigno
     * @return
     */
    @Override
    public InstrumentparamconfigDTO selectInstrumentparamconfigInfo(String instrumentparamconfigno) {
        return instrumentparamconfigRepository.selectInstrumentparamconfigInfo(instrumentparamconfigno);
    }

    /**
     * 查询所有的信息
     *
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> selectInstrumentparamconfigAllInfo() {
        return instrumentparamconfigRepository.selectInstrumentparamconfigAllInfo();
    }

    /**
     * 更新最新一次的报警时间
     *  @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime             报警时间
     */
    @Override
    public void editWarningTime(String instrumentParamConfigNo, String warningTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(warningTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        instrumentparamconfigRepository.editWarningTime(instrumentParamConfigNo,date);
    }

    /**
     * 查询状态为1的数量
     *
     * @param instrumentNo
     * @return
     */
    @Override
    public int selectProbeStateCount(String instrumentNo) {
        return instrumentparamconfigRepository.count(Wrappers.lambdaQuery(new InstrumentparamconfigPo())
                .eq(InstrumentparamconfigPo::getInstrumentno,instrumentNo)
                .eq(InstrumentparamconfigPo::getState,"1"));
    }

    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> getInstrumentParamConfigInfo(String equipmentNo) {
        return instrumentparamconfigRepository.getInstrumentParamConfigInfo(equipmentNo);
    }

    /**
     * @param probeList
     */
    @Override
    public void insertBatch(List<InstrumentparamconfigDTO> probeList) {
        instrumentparamconfigRepository.insertBatch(probeList);
    }

    /**
     * @param list
     */
    @Override
    public void updateBatch(List<InstrumentparamconfigDTO> list) {
        List<InstrumentparamconfigPo> convert = BeanConverter.convert(list, InstrumentparamconfigPo.class);
        instrumentparamconfigRepository.updateBatchById(convert);
    }

    /**
     * 获取设备已添加探头监测类型
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public List<String> getEquipmentAddProbeInfo(String equipmentNo) {
        return instrumentparamconfigRepository.getEquipmentAddProbeInfo(equipmentNo);
    }

    @Override
    public void updateProbeAlarmState(String instrumentParamConfigNo, String warningPhone) {
        InstrumentparamconfigPo instrumentparamconfigPo = new InstrumentparamconfigPo();
        instrumentparamconfigPo.setInstrumentparamconfigno(instrumentParamConfigNo);
        instrumentparamconfigPo.setWarningphone(warningPhone);
        instrumentparamconfigRepository.updateById(instrumentparamconfigPo);
    }

    @Override
    public void batchUpdateProbeAlarmState(String warningPhone, String equipmentNo) {
        instrumentparamconfigRepository.batchUpdateProbeAlarmState(warningPhone,equipmentNo);
    }

    @Override
    public List<InstrumentparamconfigDTO> getInstrumentParamConfigByCodeAndTypeId(String hospitalCode, String equipmentTypeId) {
        return instrumentparamconfigRepository.getInstrumentParamConfigByCodeAndTypeId(hospitalCode,equipmentTypeId);
    }

    @Override
    public void batchProbeAlarmState(List<String> probeIds, String warningPhone) {
        instrumentparamconfigRepository.batchProbeAlarmState(probeIds,warningPhone);
    }

    @Override
    public List<InstrumentparamconfigDTO> list() {
        List<InstrumentparamconfigPo> list = instrumentparamconfigRepository.list();
        return BeanConverter.convert(list,InstrumentparamconfigDTO.class);
    }

    @Override
    public void updateBatchData(List<InstrumentparamconfigDTO> probeList) {
        instrumentparamconfigRepository.updateBatchData(probeList);
    }

    @Override
    public void editHighLowLimit(InstrumentparamconfigCommand instrumentparamconfigCommand) {
        InstrumentparamconfigPo convert = BeanConverter.convert(instrumentparamconfigCommand, InstrumentparamconfigPo.class);
        instrumentparamconfigRepository.updateById(convert);
    }

    @Override
    public String getSnInfo(String instrumentParamConfigNo) {
        return instrumentparamconfigRepository.getSnInfo(instrumentParamConfigNo);
    }

    @Override
    public InstrumentparamconfigDTO getProbeInfoById(String instrumentParamConfigNo) {
        InstrumentparamconfigPo po = instrumentparamconfigRepository.getOne(Wrappers.lambdaQuery(new InstrumentparamconfigPo())
                .eq(InstrumentparamconfigPo::getInstrumentparamconfigno, instrumentParamConfigNo));
        return BeanConverter.convert(po,InstrumentparamconfigDTO.class);
    }
}
