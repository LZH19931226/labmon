package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.infrastructure.dao.MonitorEquipmentDao;
import com.hc.infrastructure.dao.MonitorEquipmentWarningTimeDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentPo;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.MonitorEquipmentRepository;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import com.hc.vo.equimenttype.WarningTimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *
 * @author hc
 */
@Repository
public class MonitorEquipmentRepositoryImpl extends ServiceImpl<MonitorEquipmentDao, MonitorEquipmentPo> implements MonitorEquipmentRepository {

    @Autowired
    private MonitorEquipmentDao monitorEquipmentDao;

    @Autowired
    private MonitorEquipmentWarningTimeDao monitorequipmentwarningtimeDao;
    /**
     * 分页查询监控设备信息
     *
     * @param page                    分页对象
     * @param monitorEquipmentCommand 监控设备参数
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentInfoList(Page<MonitorEquipmentVo> page, MonitorEquipmentCommand monitorEquipmentCommand) {
        List<MonitorEquipmentDto> monitorEquipmentDtoList =  monitorEquipmentDao.getEquipmentInfoList(page,
                monitorEquipmentCommand.getHospitalCode(),
                monitorEquipmentCommand.getEquipmentTypeId(),
                monitorEquipmentCommand.getEquipmentName(),
                monitorEquipmentCommand.getClientVisible());
        if(CollectionUtils.isEmpty(monitorEquipmentDtoList)){
            return null;
        }
        monitorEquipmentDtoList.forEach(res->{
            List<MonitorEquipmentWarningTimePo> timePoList = monitorequipmentwarningtimeDao.selectList(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimePo())
                    .eq(MonitorEquipmentWarningTimePo::getHospitalCode, res.getHospitalCode())
                    .eq(MonitorEquipmentWarningTimePo::getEquipmentId, res.getEquipmentNo())
                    .eq(MonitorEquipmentWarningTimePo::getEquipmentCategory, "EQ"));
            if(!CollectionUtils.isEmpty(timePoList)){
                List<WarningTimeVo> convert = BeanConverter.convert(timePoList, WarningTimeVo.class);
                res.setWarningTimeList(convert);
            }
        });
     return monitorEquipmentDtoList;
    }

    /**
     * 插入监控设备信息
     *
     * @param monitorEquipmentDto
     */
    @Override
    public void insertMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto) {
        MonitorEquipmentPo monitorEquipmentPo = BeanConverter.convert(monitorEquipmentDto, MonitorEquipmentPo.class);
        monitorEquipmentDao.insert(monitorEquipmentPo);
    }

    /**
     * 更新井控设备信息
     *
     * @param monitorEquipmentDto
     */
    @Override
    public void updateMonitorEquipment(MonitorEquipmentDto monitorEquipmentDto) {
        MonitorEquipmentPo monitorEquipmentPo = BeanConverter.convert(monitorEquipmentDto, MonitorEquipmentPo.class);
        monitorEquipmentDao.updateById(monitorEquipmentPo);
    }
}
