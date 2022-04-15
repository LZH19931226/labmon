package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.my.common.core.constant.enums.HospitalEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.repository.HospitalRegistrationInfoRepository;
import com.hc.service.HospitalRegistrationInfoService;
import com.hc.vo.hospital.HospitalInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医院注册信息业务层
 * @author hc
 */
@Service
public class HospitalRegistrationInfoServiceImpl implements HospitalRegistrationInfoService {

    @Autowired
    private HospitalRegistrationInfoRepository hospitalRegistrationInfoRepository;

    @Override
    public List<HospitalRegistrationInfoDto> selectHospitalInfo(Page<HospitalInfoVo> page, HospitalCommand hospitalCommand) {
        return hospitalRegistrationInfoRepository.selectHospitalInfo(page, hospitalCommand);
    }

    @Override
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        String hospitalName = hospitalCommand.getHospitalFullName();
        if(hospitalName==null||"".equals(hospitalName)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_NAME_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.insertHospitalInfo(hospitalCommand);
    }
}
