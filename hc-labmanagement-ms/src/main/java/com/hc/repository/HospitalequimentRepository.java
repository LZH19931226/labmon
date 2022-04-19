package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.HospitalequimentDTO;
import com.hc.po.HospitalequimentPo;


public interface HospitalequimentRepository extends IService <HospitalequimentPo>{


    HospitalequimentDTO selectHospitalEquiment(String hospitalcode, String equipmenttypeid);

    void saveHospitalEquiment(HospitalequimentDTO hospitalequimentDTO);
}