package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.infrastructure.dao.InstrumentparamconfigDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.InstrumentparamconfigPo;
import com.hc.repository.InstrumentparamconfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


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

    /**
     * 获取仪器信息集合
     *
     * @param instrumentNo
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> slectinfo(String instrumentNo) {

        List<InstrumentparamconfigPo> instrumentparamconfigPoList = instrumentparamconfigDao.selectList(Wrappers.lambdaQuery(new InstrumentparamconfigPo()).eq(InstrumentparamconfigPo::getInstrumentno, instrumentNo));

        return BeanConverter.convert(instrumentparamconfigPoList,InstrumentparamconfigDTO.class);
    }

    @Override
    public List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo) {
        return instrumentparamconfigDao.selectInstrumentparamconfigByEqNo(equipmentNo);
    }
}