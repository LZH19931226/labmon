package com.hc.repository.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.infrastructure.dao.MonitorEquipmentWarningTimeDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * @author hc
 */
@Repository
public class MonitorequipmentwarningtimeRepositoryImpl extends ServiceImpl<MonitorEquipmentWarningTimeDao,MonitorEquipmentWarningTimePo> implements MonitorequipmentwarningtimeRepository {

    @Autowired
    private MonitorEquipmentWarningTimeDao monitorEquipmentWarningTimeDao;

    /**
     * 插入报警时段
     *
     * @param monitorequipmentwarningtimeDTO
     */
    @Override
    public void insetWarningtimeList(List<MonitorequipmentwarningtimeDTO> monitorequipmentwarningtimeDTO) {
        List<MonitorEquipmentWarningTimePo> warningTimePo = BeanConverter.convert(monitorequipmentwarningtimeDTO, MonitorEquipmentWarningTimePo.class);
        monitorEquipmentWarningTimeDao.insertBatchSomeColumn(warningTimePo);
    }

    /**
     * 删除报警时段
     *
     * @param monitorequipmentwarningtimeDTO
     */
    @Override
    public void deleteInfo(MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO) {
        monitorEquipmentWarningTimeDao.deleteById(monitorequipmentwarningtimeDTO.getTimeblockid());
    }

    /**
     * 获取报警时段集合
     *
     * @param hospitalCode
     * @param equipmentNo
     * @return
     */
    @Override
    public List<MonitorequipmentwarningtimeDTO> selectWarningtimeByHosCodeAndEno(String hospitalCode, String equipmentNo) {
        List<MonitorEquipmentWarningTimePo> timePoList = monitorEquipmentWarningTimeDao.selectList(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimePo())
                .eq(MonitorEquipmentWarningTimePo::getHospitalCode, hospitalCode)
                .eq(MonitorEquipmentWarningTimePo::getEquipmentId, equipmentNo));
        return BeanConverter.convert(timePoList,MonitorequipmentwarningtimeDTO.class);
    }

    /**
     * 获取报警时段
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public Integer selectWarningtimeByEno(String equipmentNo) {

        return monitorEquipmentWarningTimeDao.selectCount(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimePo())
                .eq(MonitorEquipmentWarningTimePo::getEquipmentId, equipmentNo));
    }

    /**
     * 获取超时集合
     *
     * @param equipmentNoList
     * @return
     */
    @Override
    public List<MonitorequipmentwarningtimeDTO> selectWarningtimeByEnoList(List<String> equipmentNoList) {
        List<MonitorEquipmentWarningTimePo> timePoList = monitorEquipmentWarningTimeDao.selectList(Wrappers.lambdaQuery(new MonitorEquipmentWarningTimePo())
                .in(MonitorEquipmentWarningTimePo::getEquipmentId, equipmentNoList));
        return BeanConverter.convert(timePoList,MonitorequipmentwarningtimeDTO.class);
    }
}
