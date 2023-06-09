package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.command.labmanagement.model.hospital.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.repository.HospitalRegistrationInfoRepository;
import com.hc.service.HospitalRegistrationInfoService;
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

    /**
     * 分页获取医院信息集合
     * @param page 分页对象
     * @param hospitalCommand 医院信息数据传输对象
     * @return
     */
    @Override
    public List<HospitalRegistrationInfoDto> selectHospitalInfo(Page<HospitalInfoVo> page, HospitalCommand hospitalCommand) {
        return hospitalRegistrationInfoRepository.selectHospitalInfo(page, hospitalCommand);
    }

    /**
     * 查询医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    @Override
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        String hospitalName = hospitalCommand.getHospitalFullName();
        if(StringUtils.isBlank(hospitalName)){
            throw new IedsException(LabSystemEnum.HOSPITAL_FULL_NAME_NOT_NULL);
        }
        hospitalRegistrationInfoRepository.insertHospitalInfo(hospitalCommand);
    }

    /**
     * 修改医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    @Override
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        String hospitalCode = hospitalCommand.getHospitalCode();
        String hospitalName = hospitalCommand.getHospitalName();
        if(StringUtils.isBlank(hospitalCode)){
            throw new IedsException(LabSystemEnum.HOSPITAL_CODE_NOT_NULL);
        }
        if(StringUtils.isBlank(hospitalName)){
            throw new IedsException(LabSystemEnum.HOSPITAL_FULL_NAME_NOT_NULL);
        }
        hospitalRegistrationInfoRepository.editHospitalInfo(hospitalCommand);
    }

    /**
     * 删除医院信息
     * @param hospitalCode 医院编码
     */
    @Override
    public void deleteHospitalInfoByCode(String hospitalCode) {
        if(StringUtils.isBlank(hospitalCode)){
            throw new IedsException(LabSystemEnum.HOSPITAL_CODE_NOT_NULL);
        }
        hospitalRegistrationInfoRepository.deleteHospitalInfoByCode(hospitalCode);
    }

    /**
     * 查询医院名称集合
     * @return 医院名称集合
     */
    @Override
    public List<HospitalRegistrationInfoDto> selectHospitalNameList() {
        return hospitalRegistrationInfoRepository.selectHospitalNameList();
    }

    /**
     * 通过医院id获取医院信息对象
     * @param hospitalCode 医院id
     * @return 医院信息
     */
    @Override
    public HospitalRegistrationInfoDto findHospitalInfoByCode(String hospitalCode) {
        return hospitalRegistrationInfoRepository.findHospitalInfoByCode(hospitalCode);
    }

    /**
     * 获取医院code集合
     *
     * @return 医院id集合
     */
    @Override
    public List<String> selectHospitalCodeList() {
        return hospitalRegistrationInfoRepository.selectHospitalCodeList();
    }

    /**
     * 获取医院信息
     * @return 医院信息集合
     */
    @Override
    public List<HospitalRegistrationInfoDto> getAllHospitalInfo() {
        return hospitalRegistrationInfoRepository.getAllHospitalInfo();
    }
}
