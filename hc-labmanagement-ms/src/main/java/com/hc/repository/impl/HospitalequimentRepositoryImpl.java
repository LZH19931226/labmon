package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.HospitalequimentDTO;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.repository.HospitalequimentRepository;
import com.hc.infrastructure.dao.HospitalequimentDao;
import com.hc.po.HospitalequimentPo;


@Repository
public class HospitalequimentRepositoryImpl extends ServiceImpl<HospitalequimentDao,HospitalequimentPo> implements HospitalequimentRepository  {

    @Autowired
    private HospitalequimentDao hospitalequimentDao;


    @Override
    public HospitalequimentDTO selectHospitalEquiment(String hospitalcode, String equipmenttypeid) {
        HospitalequimentPo hospitalequimentPo = hospitalequimentDao.selectOne(Wrappers.lambdaQuery(new HospitalequimentPo())
                .eq(HospitalequimentPo::getHospitalcode, hospitalcode)
                .eq(HospitalequimentPo::getEquipmenttypeid, equipmenttypeid));
        return BeanConverter.convert(hospitalequimentPo,HospitalequimentDTO.class);
    }

    @Override
    public void saveHospitalEquiment(HospitalequimentDTO hospitalequimentDTO) {
        HospitalequimentPo hospitalequimentPo = BeanConverter.convert(hospitalequimentDTO, HospitalequimentPo.class);
        hospitalequimentDao.insert(hospitalequimentPo);
    }
}