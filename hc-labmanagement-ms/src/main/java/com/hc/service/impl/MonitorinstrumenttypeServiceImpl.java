package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.MonitorInstrumentTypeCommand;
import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.po.MonitorinstrumenttypePo;
import com.hc.repository.MonitorinstrumenttypeRepository;
import com.hc.service.MonitorinstrumenttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalInt;

@Service
public class MonitorinstrumenttypeServiceImpl implements MonitorinstrumenttypeService {

    @Autowired
    private MonitorinstrumenttypeRepository monitorinstrumenttypeRepository;
    /**
     * 查询监控设备类型信息
     *
     * @param instrumenttypeid
     * @return
     */
    @Override
    public MonitorequipmenttypeDTO selectinfo(Integer instrumenttypeid) {
       return monitorinstrumenttypeRepository.selectinfoByTypeid(instrumenttypeid);
    }

    /**
     * 查询所有的监控设备类型信息
     *
     * @return
     */
    @Override
    public List<MonitorinstrumenttypeDTO> selectAll() {
        return monitorinstrumenttypeRepository.seleclAll();
    }

    /**
     * 添加设备
     *
     * @param monitorInstrumentTypeCommand
     */
    @Override
    public void add(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand) {
        String instrumentTypeName = monitorInstrumentTypeCommand.getInstrumentTypeName();
        int count = monitorinstrumenttypeRepository.count(Wrappers.lambdaQuery(new MonitorinstrumenttypePo())
                .eq(MonitorinstrumenttypePo::getInstrumenttypename, instrumentTypeName));
        if(count>0){
            throw new IedsException(LabSystemEnum.NAME_ALREADY_EXISTS);
        }
        MonitorinstrumenttypePo monitorInstrumentTypePo =  buildMonitorinstrumenttypePo(monitorInstrumentTypeCommand);
        List<MonitorinstrumenttypePo> list = monitorinstrumenttypeRepository.list();
        if (CollectionUtils.isNotEmpty(list)) {
            OptionalInt max = list.stream().mapToInt(MonitorinstrumenttypePo::getInstrumenttypeid).max();
            if(max.isPresent()){
                monitorInstrumentTypePo.setInstrumenttypeid(max.getAsInt()+1);
            }else{
                monitorInstrumentTypePo.setInstrumenttypeid(list.size()+1);
            }
        }else {
            monitorInstrumentTypePo.setInstrumenttypeid(1);
        }
        monitorinstrumenttypeRepository.save(monitorInstrumentTypePo);
    }

    private MonitorinstrumenttypePo buildMonitorinstrumenttypePo(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand) {
        List<String> list = monitorInstrumentTypeCommand.getEquipmentTypeId();
        String equipmentTypeId = String.join(",", list);
        MonitorinstrumenttypePo monitorInstrumentTypePo = new MonitorinstrumenttypePo();
        monitorInstrumentTypePo.setEquipmenttypeid(equipmentTypeId);
        monitorInstrumentTypePo.setInstrumenttypename(monitorInstrumentTypeCommand.getInstrumentTypeName());
        return monitorInstrumentTypePo;
    }

    /**
     * 分页获取监控设备类型信息
     *
     * @param page
     * @param instrumentTypeName
     * @return
     */
    @Override
    public List<MonitorinstrumenttypeDTO> listByPage(Page<MonitorinstrumenttypeDTO> page, String instrumentTypeName) {
        return monitorinstrumenttypeRepository.listByPage(page,instrumentTypeName);
    }

    /**
     * 修改监控设备类型信息
     *
     * @param monitorInstrumentTypeCommand
     */
    @Override
    public void edit(MonitorInstrumentTypeCommand monitorInstrumentTypeCommand) {
        Integer instrumentTypeId = monitorInstrumentTypeCommand.getInstrumentTypeId();
        MonitorinstrumenttypePo monitorInstrumentTypePo = monitorinstrumenttypeRepository.getById(instrumentTypeId);
        String instrumentTypeName = monitorInstrumentTypePo.getInstrumenttypename();
        List<String> list = monitorInstrumentTypeCommand.getEquipmentTypeId();
        String equipmentTypeId = String.join(",", list);
        String typeName = monitorInstrumentTypeCommand.getInstrumentTypeName();
        //判断名称的唯一
        if (!instrumentTypeName.equals(typeName)) {
            int count = monitorinstrumenttypeRepository.count(Wrappers.lambdaQuery(new MonitorinstrumenttypePo())
                    .eq(MonitorinstrumenttypePo::getInstrumenttypename, typeName));
            if (count > 0) {
                throw new IedsException(LabSystemEnum.NAME_ALREADY_EXISTS);
            }
            monitorInstrumentTypePo.setInstrumenttypename(typeName);
        }
        monitorInstrumentTypePo.setEquipmenttypeid(equipmentTypeId);
        monitorinstrumenttypeRepository.updateById(monitorInstrumentTypePo);
    }

    /**
     * 删除
     *
     * @param instrumentTypeId
     */
    @Override
    public void remove(String instrumentTypeId) {
        monitorinstrumenttypeRepository.removeById(instrumentTypeId);
    }
}
