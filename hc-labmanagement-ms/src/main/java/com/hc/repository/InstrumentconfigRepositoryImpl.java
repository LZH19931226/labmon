package com.hc.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.InstrumentconfigDTO;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.repository.InstrumentconfigRepository;
import com.hc.infrastructure.dao.InstrumentconfigDao;
import com.hc.po.InstrumentconfigPo;

import java.util.List;


@Repository
public class InstrumentconfigRepositoryImpl extends ServiceImpl<InstrumentconfigDao,InstrumentconfigPo> implements InstrumentconfigRepository  {

    @Autowired
    private InstrumentconfigDao instrumentconfigDao;
    /**
     * 查询探头配置信息
     *
     * @param instrumentconfigid
     * @return
     */
    @Override
    public InstrumentconfigDTO selectInfoByConfigid(Integer instrumentconfigid) {
        InstrumentconfigPo instrumentconfigPo = instrumentconfigDao.selectOne(Wrappers.lambdaQuery(new InstrumentconfigPo()).eq(InstrumentconfigPo::getInstrumentconfigid, instrumentconfigid));
        return BeanConverter.convert(instrumentconfigPo,InstrumentconfigDTO.class);
    }

    /**
     * 查询所有的探头配置信息
     *
     * @return
     */
    @Override
    public List<InstrumentconfigDTO> selectAllInfo() {
        List<InstrumentconfigPo> instrumentconfigPos = instrumentconfigDao.selectList(null);
        return BeanConverter.convert(instrumentconfigPos,InstrumentconfigDTO.class);
    }
}
