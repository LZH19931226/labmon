package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.command.labmanagement.hospital.HospitalCommand;
import com.hc.constant.HospitalEnumErrorCode;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.my.common.core.exception.IedsException;
import com.hc.repository.HospitalRegistrationInfoRepository;
import com.hc.vo.hospital.HospitalInfoVo;
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
    public List<HospitalRegistrationInfoDto> selectHospitalInfo(Page<HospitalInfoVo> page, HospitalCommand hospitalCommand) {
        return hospitalRegistrationInfoRepository.selectHospitalInfo(page, hospitalCommand);
    }

    @Override
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        String hospitalName = hospitalCommand.getHospitalFullName();
        if(StringUtils.isBlank(hospitalName)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_FULL_NAME_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.insertHospitalInfo(hospitalCommand);
        //添加同步缓存
        HospitalRegistrationInfoDto hospitalRegistrationInfoDto =
                hospitalRegistrationInfoRepository.selectHospitalInfoByHospitalName(hospitalCommand.getHospitalName());
    }

    @Override
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        String hospitalCode = hospitalCommand.getHospitalCode();
        String hospitalName = hospitalCommand.getHospitalName();
        if(StringUtils.isBlank(hospitalCode)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_CODE_NOT_NULL.getCode());
        }
        if(StringUtils.isBlank(hospitalName)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_FULL_NAME_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.editHospitalInfo(hospitalCommand);
        //更新同步缓存
        HospitalRegistrationInfoDto hospitalRegistrationInfoDto =
                hospitalRegistrationInfoRepository.selectHospitalInfoByHospitalName(hospitalCommand.getHospitalName());
    }

    @Override
    public void deleteHospitalInfoByCode(String hospitalCode) {
        if(StringUtils.isBlank(hospitalCode)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_CODE_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.deleteHospitalInfoByCode(hospitalCode);
    }

    @Override
    public List<HospitalRegistrationInfoDto> selectHospitalNameList() {
        return hospitalRegistrationInfoRepository.selectHospitalNameList();
    }

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public HospitalRegistrationInfoDto findHospitalInfoByCode(String hospitalCode) {
        return hospitalRegistrationInfoRepository.findHospitalInfoByCode(hospitalCode);
    }

    /**
     * 获取医院code集合
     *
     * @return
     */
    @Override
    public List<String> selectHospitalCodeList() {
        return hospitalRegistrationInfoRepository.selectHospitalCodeList();
    }

    @Override
    public List<HospitalRegistrationInfoDto> getAllHospitalInfo() {
        return hospitalRegistrationInfoRepository.getAllHospitalInfo();
    }
}
