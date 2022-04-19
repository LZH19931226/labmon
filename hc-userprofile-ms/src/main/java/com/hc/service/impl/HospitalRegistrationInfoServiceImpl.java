package com.hc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.my.common.core.constant.enums.HospitalEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.repository.HospitalRegistrationInfoRepository;
import com.hc.service.HospitalRegistrationInfoService;
import com.redis.util.RedisTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

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

        //添加同步缓存
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        objectObjectObjectHashOperations.put("hospital:info",hospitalCommand.getHospitalCode(), JSON.toJSON(hospitalCommand));
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

        //修改同步缓存
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        objectObjectObjectHashOperations.delete("hospital:info",hospitalCommand.getHospitalCode());
        objectObjectObjectHashOperations.put("hospital:info",hospitalCommand.getHospitalCode(), JSON.toJSON(hospitalCommand));
    }

    @Override
    public void deleteHospitalInfoByCode(String hospitalCode) {
        if(StringUtils.isBlank(hospitalCode)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_CODE_NOT_NULL.getCode());
        }
        hospitalRegistrationInfoRepository.deleteHospitalInfoByCode(hospitalCode);
        //删除同步缓存
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        objectObjectObjectHashOperations.delete("hospital:info",hospitalCode);
    }

    @Override
    public List<HospitalRegistrationInfoDto> selectHospitalNameList() {
        return hospitalRegistrationInfoRepository.selectHospitalNameList();
    }
}
