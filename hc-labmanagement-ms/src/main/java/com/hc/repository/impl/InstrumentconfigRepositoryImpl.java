package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.repository.InstrumentconfigRepository;
import com.hc.infrastructure.dao.InstrumentConfigDao;
import com.hc.po.InstrumentconfigPo;

import java.util.List;


@Repository
public class InstrumentconfigRepositoryImpl extends ServiceImpl<InstrumentConfigDao,InstrumentconfigPo> implements InstrumentconfigRepository  {

    @Autowired
    private InstrumentConfigDao instrumentconfigDao;
    /**
     * 查询探头配置信息
     *
     * @param instrumentconfigid
     * @return
     */
    @Override
    public InstrumentConfigDTO selectInfoByConfigid(Integer instrumentconfigid) {
        InstrumentconfigPo instrumentconfigPo = instrumentconfigDao.selectOne(Wrappers.lambdaQuery(new InstrumentconfigPo()).eq(InstrumentconfigPo::getInstrumentconfigid, instrumentconfigid));
        return BeanConverter.convert(instrumentconfigPo, InstrumentConfigDTO.class);
    }

    /**
     * 查询所有的探头配置信息
     *
     * @return
     */
    @Override
    public List<InstrumentConfigDTO> selectAllInfo() {
        List<InstrumentconfigPo> instrumentconfigPos = instrumentconfigDao.selectList(null);
        return BeanConverter.convert(instrumentconfigPos, InstrumentConfigDTO.class);
    }

    /**
     * 分页查询监控参数类型信息
     * @param page
     * @param instrumentConfigName
     * @return
     */
    @Override
    public List<InstrumentConfigDTO> listByPage(Page<InstrumentConfigDTO> page, String instrumentConfigName) {
        return instrumentconfigDao.listByPage(page,instrumentConfigName);
    }
}
