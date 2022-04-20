package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.po.HospitalequimentPo;

import java.util.List;


public interface HospitalequimentRepository extends IService <HospitalequimentPo>{


    HospitalequimentDTO selectHospitalEquiment(String hospitalcode, String equipmenttypeid);

    void saveHospitalEquiment(HospitalequimentDTO hospitalequimentDTO);

    void updateHospitalEquiment(HospitalequimentDTO hospitalequimentDTO);

    List<HospitalequimentDTO> selectHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand);
}