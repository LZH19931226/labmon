package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.InstrumentparamconfigCommand;
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

    /**
     * 删除探头参数信息
     *
     * @param instrumentno
     */
    @Override
    public void deleteInfoByEno(String instrumentno) {
        instrumentparamconfigDao.delete(Wrappers.lambdaQuery(new InstrumentparamconfigPo()).eq(InstrumentparamconfigPo::getInstrumentno,instrumentno));
    }

    @Override
    public List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(String equipmentNo) {
        return instrumentparamconfigDao.selectInstrumentparamconfigByEqNo(equipmentNo);
    }

    /**
     * 更新探头配置信息
     *
     * @param instrumentparamconfigDTO
     */
    @Override
    public void updateInfo(InstrumentparamconfigDTO instrumentparamconfigDTO) {
        InstrumentparamconfigPo instrumentparamconfigPo = BeanConverter.convert(instrumentparamconfigDTO, InstrumentparamconfigPo.class);
        instrumentparamconfigDao.updateById(instrumentparamconfigPo);
    }

    /**
     * 删除探头信息
     *
     * @param instrumentparamconfigno
     */
    @Override
    public void deleteInstrumentparamconfig(InstrumentparamconfigDTO instrumentparamconfigno) {
        InstrumentparamconfigPo instrumentparamconfigPo = BeanConverter.convert(instrumentparamconfigno, InstrumentparamconfigPo.class);
        instrumentparamconfigDao.deleteById(instrumentparamconfigPo);
    }

    /**
     * 分页获取探头信息
     *
     * @param page
     * @param instrumentparamconfigCommand
     * @return
     */
    @Override
    public List<InstrumentparamconfigDTO> findInstrumentparamconfig(Page page, InstrumentparamconfigCommand instrumentparamconfigCommand) {
        return instrumentparamconfigDao.findInstrumentparamconfig(page,instrumentparamconfigCommand.getHospitalCode(),instrumentparamconfigCommand.getEquipmentTypeName(),
                                                                    instrumentparamconfigCommand.getInstrumentname(), instrumentparamconfigCommand.getSn());

    }
}