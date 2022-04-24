package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.infrastructure.dao.InstrumentparamconfigDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.InstrumentparamconfigPo;
import com.hc.repository.InstrumentparamconfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class InstrumentparamconfigRepositoryImpl extends ServiceImpl<InstrumentparamconfigDao,InstrumentparamconfigPo> implements InstrumentparamconfigRepository  {

    @Autowired
    private InstrumentparamconfigDao instrumentparamconfigDao;

    /**
     * 插入探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void insertInstrumentmonitor(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        InstrumentparamconfigPo instrumentparamconfigPo = BeanConverter.convert(instrumentparamconfigDTO, InstrumentparamconfigPo.class);
        instrumentparamconfigDao.insert(instrumentparamconfigPo);
    }
}