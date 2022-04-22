package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.infrastructure.dao.HospitalequimentDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalequimentPo;
import com.hc.repository.HospitalequimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class HospitalequimentRepositoryImpl extends ServiceImpl<HospitalequimentDao, HospitalequimentPo> implements HospitalequimentRepository {

    @Autowired
    private HospitalequimentDao hospitalequimentDao;


    @Override
    public HospitalequimentDTO selectHospitalEquiment(String hospitalcode, String equipmenttypeid) {
        HospitalequimentPo hospitalequimentPo = hospitalequimentDao.selectOne(Wrappers.lambdaQuery(new HospitalequimentPo())
                .eq(HospitalequimentPo::getHospitalcode, hospitalcode)
                .eq(HospitalequimentPo::getEquipmenttypeid, equipmenttypeid));
        return BeanConverter.convert(hospitalequimentPo, HospitalequimentDTO.class);
    }

    @Override
    public void saveHospitalEquiment(HospitalequimentDTO hospitalequimentDTO) {
        HospitalequimentPo hospitalequimentPo = BeanConverter.convert(hospitalequimentDTO, HospitalequimentPo.class);
        hospitalequimentDao.insert(hospitalequimentPo);
    }

    @Override
    public void updateHospitalEquiment(HospitalequimentDTO hospitalequimentDTO) {
        HospitalequimentPo hospitalequimentPo = BeanConverter.convert(hospitalequimentDTO, HospitalequimentPo.class);
        hospitalequimentDao.update(hospitalequimentPo, Wrappers.lambdaUpdate(new HospitalequimentPo()).
                eq(HospitalequimentPo::getEquipmenttypeid, hospitalequimentPo.getEquipmenttypeid())
                .eq(HospitalequimentPo::getHospitalcode, hospitalequimentPo.getHospitalcode()));
    }

    @Override
    public List<HospitalequimentDTO> selectHospitalEquimentType(Page page, HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        return hospitalequimentDao.selectHospitalEquimentType(page,hospitalEquimentTypeCommand);
    }
}