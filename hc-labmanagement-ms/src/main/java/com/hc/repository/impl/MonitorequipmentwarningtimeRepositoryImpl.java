package com.hc.repository.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.infrastructure.dao.MonitorEquipmentWarningTimeDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 *
 * @author hc
 */
@Repository
public class MonitorequipmentwarningtimeRepositoryImpl extends ServiceImpl<MonitorEquipmentWarningTimeDao,MonitorEquipmentWarningTimePo> implements MonitorequipmentwarningtimeRepository  {

    @Autowired
    private MonitorEquipmentWarningTimeDao monitorEquipmentWarningTimeDao;

    /**
     * 插入报警时段
     *
     * @param monitorequipmentwarningtimeDTO
     */
    @Override
    public void insetWarningtimeList(MonitorequipmentwarningtimeDTO monitorequipmentwarningtimeDTO) {
        MonitorEquipmentWarningTimePo warningTimePo = BeanConverter.convert(monitorequipmentwarningtimeDTO, MonitorEquipmentWarningTimePo.class);
        monitorEquipmentWarningTimeDao.insert(warningTimePo);
    }
}