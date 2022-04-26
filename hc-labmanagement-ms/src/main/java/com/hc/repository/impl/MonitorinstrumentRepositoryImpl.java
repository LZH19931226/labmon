package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.MonitorinstrumentDTO;
import com.hc.infrastructure.dao.MonitorinstrumentDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.MonitorinstrumentPo;
import com.hc.repository.MonitorinstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class MonitorinstrumentRepositoryImpl extends ServiceImpl<MonitorinstrumentDao,MonitorinstrumentPo> implements MonitorinstrumentRepository  {

    @Autowired
    private MonitorinstrumentDao monitorinstrumentDao;
    /**
     * 通过sn和hospitalCode查询sn是否被占用
     *
     * @param monitorinstrumentDTO
     * @return
     */
    @Override
    public Integer selectCount(MonitorinstrumentDTO monitorinstrumentDTO) {

        return monitorinstrumentDao.selectCount(Wrappers.lambdaQuery(new MonitorinstrumentPo())
                .eq(MonitorinstrumentPo::getSn,monitorinstrumentDTO.getSn())
                .eq(MonitorinstrumentPo::getHospitalcode,monitorinstrumentDTO.getHospitalcode()));
    }

    /**
     * 查询监控信息
     *
     * @param equipmentNo 设备id
     * @return
     */
    @Override
    public MonitorinstrumentDTO selectMonitorByEno(String equipmentNo) {
        MonitorinstrumentPo monitorinstrumentPos
                = monitorinstrumentDao.selectOne(Wrappers.lambdaQuery(new MonitorinstrumentPo()).eq(MonitorinstrumentPo::getEquipmentno, equipmentNo));
        return BeanConverter.convert(monitorinstrumentPos,MonitorinstrumentDTO.class);
    }

    /**
     * 更新监控信息
     *
     * @param monitorinstrumentDTO
     */
    @Override
    public void updateMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO) {
        MonitorinstrumentPo monitorinstrumentPo = BeanConverter.convert(monitorinstrumentDTO, MonitorinstrumentPo.class);
        monitorinstrumentDao.updateById(monitorinstrumentPo);
    }

    /**
     * 插入监控信息
     *
     * @param monitorinstrumentDTO
     */
    @Override
    public void insertMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO) {
        MonitorinstrumentPo monitorinstrumentPo = BeanConverter.convert(monitorinstrumentDTO, MonitorinstrumentPo.class);
        monitorinstrumentDao.insert(monitorinstrumentPo);
    }

    /**
     * 删除监控探头信息
     *
     * @param equipmentNo
     */
    @Override
    public void deleteMonitorinstrumentInfo(String equipmentNo) {
        monitorinstrumentDao.deleteById(equipmentNo);
    }
}