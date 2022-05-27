package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.po.HospitalequimentPo;
import com.hc.vo.equimenttype.HospitalequimentVo;

import java.util.List;


public interface HospitalequimentRepository extends IService <HospitalequimentPo>{


    HospitalequimentDTO selectHospitalEquiment(String hospitalcode, String equipmenttypeid);

    void saveHospitalEquiment(HospitalequimentDTO hospitalequimentDTO);

    void updateHospitalEquiment(HospitalequimentDTO hospitalequimentDTO);

    List<HospitalequimentDTO> selectHospitalEquimentType(Page<HospitalequimentVo> page, HospitalEquimentTypeCommand hospitalEquimentTypeCommand);

    /**
     * 查询医院设备信息
     * @param hospitalCode
     * @param equipmentTypeId
     * @return
     */
    HospitalequimentDTO selectHospitalEquimentInfoByCodeAndTypeId(String hospitalCode, String equipmentTypeId);

    /**
     * 查询医院设备信息集合
     * @param hospitalCode
     * @return
     */
    List<HospitalequimentDTO> findHospitalEquipmentTypeByCode(String hospitalCode);

    /**
     * 查询所有的设备信息集合
     * @return
     */
    List<HospitalequimentDTO> getAllHospitalEquipmentTypeInfo();
}
