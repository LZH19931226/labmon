package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.MonitorinstrumentDTO;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.repository.MonitorinstrumentRepository;
import com.hc.infrastructure.dao.MonitorinstrumentDao;
import com.hc.po.MonitorinstrumentPo;


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
     * 插入监控仪器信息
     *
     * @param monitorinstrumentDTO
     */
    @Override
    public void insertMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO) {
        MonitorinstrumentPo monitorinstrumentPo = BeanConverter.convert(monitorinstrumentDTO, MonitorinstrumentPo.class);
        monitorinstrumentDao.insert(monitorinstrumentPo);
    }
}