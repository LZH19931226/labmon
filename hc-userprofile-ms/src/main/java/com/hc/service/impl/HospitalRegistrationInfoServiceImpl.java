package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.my.common.core.constant.enums.HospitalEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.repository.HospitalRegistrationInfoRepository;
import com.hc.service.HospitalRegistrationInfoService;
import org.apache.commons.lang3.StringUtils;
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
    public List<HospitalRegistrationInfoDto> selectHospitalInfo(Page page, HospitalCommand hospitalCommand) {
        return hospitalRegistrationInfoRepository.selectHospitalInfo(page, hospitalCommand);
    }

    @Override
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        String hospitalName = hospitalCommand.getHospitalFullName();
        if(StringUtils.isBlank(hospitalName)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_FULL_NAME_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.insertHospitalInfo(hospitalCommand);
    }

    @Override
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        String hospitalCode = hospitalCommand.getHospitalCode();
        String hospitalFullName = hospitalCommand.getHospitalFullName();
        if(StringUtils.isBlank(hospitalCode)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_CODE_NOT_NULL.getCode());
        }
        if(StringUtils.isBlank(hospitalFullName)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_FULL_NAME_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.editHospitalInfo(hospitalCommand);
    }

    @Override
    public void deleteHospitalInfoByCode(String hospitalCode) {
        if(StringUtils.isBlank(hospitalCode)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_CODE_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.deleteHospitalInfoByCode(hospitalCode);
    }
}
