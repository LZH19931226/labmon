package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentConfigCommand;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.InstrumentconfigPo;
import com.hc.repository.InstrumentconfigRepository;
import com.hc.service.InstrumentConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Service
public class InstrumentconfigServiceImpl implements InstrumentConfigService {

    @Autowired
    private InstrumentconfigRepository instrumentconfigRepository;
    /**
     * 查询探头配置信息
     *
     * @param instrumentconfigid
     * @return
     */
    @Override
    public InstrumentConfigDTO selectInfoByConfigid(Integer instrumentconfigid) {
        return instrumentconfigRepository.selectInfoByConfigid(instrumentconfigid);
    }

    /**
     * 查询所有的探头配置信息
     *
     * @return
     */
    @Override
    public List<InstrumentConfigDTO> selectAllInfo() {
        return instrumentconfigRepository.selectAllInfo();
    }

    /**
     * @param instrumentConfigName
     */
    @Override
    public void save(String instrumentConfigName) {
        List<InstrumentconfigPo> list = instrumentconfigRepository.list();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        List<String> instrumentConfigNameList = list.stream().map(InstrumentconfigPo::getInstrumentconfigname).collect(Collectors.toList());
        if(instrumentConfigNameList.contains(instrumentConfigName)){
            throw new IedsException(LabSystemEnum.NAME_ALREADY_EXISTS.getMessage());
        }
        OptionalInt max = list.stream().mapToInt(InstrumentconfigPo::getInstrumentconfigid).max();
        InstrumentconfigPo instrumentconfigPo = new InstrumentconfigPo();
        if(max.isPresent()){
            instrumentconfigPo.setInstrumentconfigid(max.getAsInt()+1);
        }else {
            instrumentconfigPo.setInstrumentconfigid(list.size()+1);
        }
        instrumentconfigPo.setInstrumentconfigname(instrumentConfigName);
        instrumentconfigRepository.save(instrumentconfigPo);
    }

    /**
     * 分页查询监控参数类型信息
     * @param page
     * @param instrumentConfigName
     * @return
     */
    @Override
    public List<InstrumentConfigDTO> listByPage(Page<InstrumentConfigDTO> page, String instrumentConfigName) {
        return instrumentconfigRepository.listByPage(page,instrumentConfigName);
    }

    /**
     * @param instrumentConfigCommand
     */
    @Override
    public void edit(InstrumentConfigCommand instrumentConfigCommand) {
        String instrumentConfigName = instrumentConfigCommand.getInstrumentConfigName();
        Integer instrumentConfigId = instrumentConfigCommand.getInstrumentConfigId();
        InstrumentConfigDTO instrumentConfigDTO = instrumentconfigRepository.selectInfoByConfigid(instrumentConfigId);
        String oldName = instrumentConfigDTO.getInstrumentconfigname();
        if (instrumentConfigName.equals(oldName)) {
            return;
        }
        int count = instrumentconfigRepository.count(Wrappers.lambdaQuery(new InstrumentconfigPo())
                .eq(InstrumentconfigPo::getInstrumentconfigname, instrumentConfigName));
        if(count>0){

            throw new IedsException(LabSystemEnum.NAME_ALREADY_EXISTS.getMessage());
        }else {
            InstrumentconfigPo instrumentconfigPo = new InstrumentconfigPo();
            instrumentconfigPo.setInstrumentconfigid(instrumentConfigId);
            instrumentconfigPo.setInstrumentconfigname(instrumentConfigName);
            instrumentconfigRepository.updateById(instrumentconfigPo);
        }
    }

    /**
     * @param instrumentConfigId
     */
    @Override
    public void remove(String instrumentConfigId) {
        instrumentconfigRepository.removeById(instrumentConfigId);
    }

    @Override
    public List<InstrumentConfigDTO> list() {
        List<InstrumentconfigPo> list = instrumentconfigRepository.list();
        return BeanConverter.convert(list,InstrumentConfigDTO.class);
    }
}
