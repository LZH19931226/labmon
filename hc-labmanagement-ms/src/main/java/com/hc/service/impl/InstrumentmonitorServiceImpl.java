package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentMonitorCommand;
import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.InstrumentmonitorPo;
import com.hc.repository.InstrumentmonitorRepository;
import com.hc.service.InstrumentmonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentmonitorServiceImpl implements InstrumentmonitorService {

    @Autowired
    private InstrumentmonitorRepository instrumentmonitorRepository;

    @Override
    public List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid) {
        return instrumentmonitorRepository.selectMonitorEquipmentType(instrumenttypeid);
    }

    /**
     * 插入监控仪器信息
     *
     * @param instrumentmonitorDTO
     */
    @Override
    public void insertInstrumentmonitorInfo(InstrumentmonitorDTO instrumentmonitorDTO) {
        instrumentmonitorRepository.insertInstrumentmonitorInfo(instrumentmonitorDTO);
    }

    /**
     * 更新监控仪器信息
     *
     * @param instrumentmonitorDTO
     */
    @Override
    public void updateInstrumentmonitor(InstrumentmonitorDTO instrumentmonitorDTO) {
        instrumentmonitorRepository.updateInstrumentmonitor(instrumentmonitorDTO);
    }

    /**
     * 判断插入的信息是否已存在
     *
     * @param instrumentmonitorDTO
     * @return
     */
    @Override
    public boolean selectOne(InstrumentmonitorDTO instrumentmonitorDTO) {
        return instrumentmonitorRepository.selectOne(instrumentmonitorDTO);
    }

    /**
     * 查询监控仪器信息
     *
     * @param instrumenttypeid
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectMonitorEquipmentList(Integer instrumenttypeid) {
        return instrumentmonitorRepository.selectMonitorEquipmentList(instrumenttypeid);
    }

    /**
     * 查询所有探头默认配置
     *
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectMonitorEquipmentAll() {
        return instrumentmonitorRepository.selectMonitorEquipmentAll();
    }

    /**
     * 查询探头监控的信息
     *
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectInstrumentMonitorInfo(String hospitalCode) {
        return instrumentmonitorRepository.selectInstrumentMonitorInfo(hospitalCode);
    }

    /**
     * @param instrumentTypeId
     * @return
     */
    @Override
    public int countByInstrumentTypeId(String instrumentTypeId) {
        return instrumentmonitorRepository.count(Wrappers.lambdaQuery(new InstrumentmonitorPo())
                .eq(InstrumentmonitorPo::getInstrumenttypeid,instrumentTypeId));
    }

    /**
     * 删除
     *
     * @param instrumentTypeId
     */
    @Override
    public void removeByTypeId(Integer instrumentTypeId) {
        instrumentmonitorRepository.remove(Wrappers.lambdaQuery(new InstrumentmonitorPo())
                .eq(InstrumentmonitorPo::getInstrumenttypeid,instrumentTypeId));
    }

    @Override
    public void add(InstrumentMonitorCommand instrumentMonitorCommand) {
        List<InstrumentmonitorDTO> dtoList = instrumentMonitorCommand.getInstrumentmonitorDTOList();
        List<InstrumentmonitorPo> poList = BeanConverter.convert(dtoList, InstrumentmonitorPo.class);
        if(CollectionUtils.isNotEmpty(dtoList)){
            instrumentmonitorRepository.saveBatch(poList);
        }
    }

    @Override
    public List<InstrumentmonitorDTO> list(Page<InstrumentmonitorDTO> page, Integer instrumentTypeId) {
        return instrumentmonitorRepository.listByPage(page,instrumentTypeId);
    }

    @Override
    public void remove(Integer instrumentTypeId, Integer instrumentConfigId) {
        instrumentmonitorRepository.remove(Wrappers.lambdaQuery(new InstrumentmonitorPo())
                .eq(InstrumentmonitorPo::getInstrumenttypeid,instrumentTypeId)
                .eq(InstrumentmonitorPo::getInstrumentconfigid,instrumentConfigId));
    }
}
