package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.infrastructure.dao.MonitorEquipmentDao;
import com.hc.infrastructure.dao.MonitorEquipmentWarningTimeDao;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentPo;
import com.hc.repository.MonitorEquipmentRepository;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
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
        List<MonitorEquipmentDto> monitorEquipmentDtoList = monitorEquipmentDao.getEquipmentInfoList(page,
                monitorEquipmentCommand.getHospitalCode(),
                monitorEquipmentCommand.getEquipmentTypeId(),
                monitorEquipmentCommand.getEquipmentName(),
                monitorEquipmentCommand.getClientVisible());
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

    /**
     * @param monitorEquipmentDto
     * @return
     */
    @Override
    public Integer selectCount(MonitorEquipmentDto monitorEquipmentDto) {
        return monitorEquipmentDao.selectCount(Wrappers.lambdaQuery(new MonitorEquipmentPo())
                .eq(MonitorEquipmentPo::getEquipmentName, monitorEquipmentDto.getEquipmentName())
                .eq(MonitorEquipmentPo::getHospitalCode, monitorEquipmentDto.getHospitalCode()));
    }

    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> selectMonitorEquipmentInfoByNo(String equipmentNo) {
        return monitorEquipmentDao.selectMonitorEquipmentInfoByNo(equipmentNo);
    }

    /**
     * 删除设备信息
     *
     * @param equipmentNo
     */
    @Override
    public void deleteMonitorEquipmentInfo(String equipmentNo) {
        monitorEquipmentDao.deleteById(equipmentNo);
    }

    @Override
    public List<MonitorEquipmentDto> getAllMonitorEquipmentInfo() {
        return monitorEquipmentDao.getAllMonitorEquipmentInfo();
    }

    /**
     * 获取设备信息
     *
     * @param hospitalCode
     * @return
     */
    @Override
    public List<MonitorEquipmentDto> getEquipmentNoList(String hospitalCode, String equipmentTypeId) {
        return monitorEquipmentDao.getEquipmentNoList(hospitalCode, equipmentTypeId);
    }

    /**
     * 查询设备信息
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public MonitorEquipmentDto selectMonitorEquipmentInfoByEno(String equipmentNo) {
        MonitorEquipmentPo monitorEquipmentPo =
                monitorEquipmentDao.selectOne(Wrappers.lambdaQuery(new MonitorEquipmentPo()).eq(MonitorEquipmentPo::getEquipmentNo, equipmentNo));
        return BeanConverter.convert(monitorEquipmentPo, MonitorEquipmentDto.class);
    }

    @Override
    public List<MonitorEquipmentDto> getMonitorEquipmentInfoByHCode(String hospitalCode) {
        List<MonitorEquipmentPo> monitorEquipmentPos =
                monitorEquipmentDao.selectList(Wrappers.lambdaQuery(new MonitorEquipmentPo()).eq(MonitorEquipmentPo::getHospitalCode, hospitalCode));
        return BeanConverter.convert(monitorEquipmentPos, MonitorEquipmentDto.class);
    }

    /**
     * @param sn
     * @return
     */
    @Override
    public Boolean checkSn(String sn) {
        return monitorEquipmentDao.checkSn(sn) > 0;
    }

    @Override
    public void updateEquipmentWarningSwitchByHospitalCodeAndEquipmentTypeId(String hospitalCode, String equipmentTypeId, String warningPhone) {
        monitorEquipmentDao.updateEquipmentWarningSwitchByHospitalCodeAndEquipmentTypeId(hospitalCode, equipmentTypeId, warningPhone);
    }

    @Override
    public List<MonitorEquipmentDto> getMonitorEquipmentList(Page<MonitorEquipmentVo> page, MonitorEquipmentCommand monitorEquipmentCommand) {
        return monitorEquipmentDao.getMonitorEquipmentList(page, monitorEquipmentCommand);
    }

    @Override
    public List<String> getHosEqTypeEqInfo(String hospitalCode, String equipmentTypeId) {
        List<MonitorEquipmentPo> monitorEquipmentPos = monitorEquipmentDao.selectList(Wrappers.lambdaQuery(new MonitorEquipmentPo())
                .eq(MonitorEquipmentPo::getEquipmentTypeId, equipmentTypeId)
                .eq(MonitorEquipmentPo::getHospitalCode, hospitalCode));
        List<String> address = new ArrayList<>();
        address.add(Context.IsCh()?"全部房间":"Full room");
        if (CollectionUtils.isEmpty(monitorEquipmentPos)) {
            return address;
        }
        Set<String> collect = monitorEquipmentPos.stream().map(MonitorEquipmentPo::getAddress).collect(Collectors.toSet());
        address.addAll(collect);
        return address.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }

    @Override
    public List<String> getSns(String equipmentNo) {
        return monitorEquipmentDao.getSns(equipmentNo);
    }
}
