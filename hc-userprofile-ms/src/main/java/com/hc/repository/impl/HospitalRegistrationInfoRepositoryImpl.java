package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.infrastructure.dao.HospitalRegistrationInfoDao;
import com.hc.my.common.core.constant.enums.HospitalEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalRegistrationInfoPo;
import com.hc.repository.HospitalRegistrationInfoRepository;
import com.hc.vo.hospital.HospitalInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author hc
 */
@Repository
public class HospitalRegistrationInfoRepositoryImpl extends ServiceImpl<HospitalRegistrationInfoDao,HospitalRegistrationInfoPo> implements HospitalRegistrationInfoRepository {

    @Autowired
    private HospitalRegistrationInfoDao hospitalRegistrationInfoDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HospitalRegistrationInfoDto> selectHospitalInfo(Page<HospitalInfoVo> page, HospitalCommand hospitalCommand) {

        Page<HospitalRegistrationInfoPo> objectPage = new Page<>(page.getCurrent(),page.getSize());
        IPage<HospitalRegistrationInfoPo> iPage = hospitalRegistrationInfoDao.selectPage(objectPage, Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .like(StringUtils.isNotBlank(hospitalCommand.getHospitalName()), HospitalRegistrationInfoPo::getHospitalName, hospitalCommand.getHospitalName())
                .eq(StringUtils.isNotBlank(hospitalCommand.getIsEnable()),HospitalRegistrationInfoPo::getIsEnable,hospitalCommand.getIsEnable())
                );
        List<HospitalRegistrationInfoPo> records = iPage.getRecords();
        return BeanConverter.convert(records, HospitalRegistrationInfoDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        HospitalRegistrationInfoPo infoPo = BeanConverter.convert(hospitalCommand, HospitalRegistrationInfoPo.class);
        HospitalRegistrationInfoPo selectOne = hospitalRegistrationInfoDao.selectOne(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalFullName, infoPo.getHospitalFullName()));
        if(null==selectOne){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_FULL_NAME_ALREADY_EXISTS.getCode());
        }
        //设置操作时间
        infoPo.setUpdateTime(new Date());
        //设置医院的UUID
        infoPo.setHospitalCode(UUID.randomUUID().toString().replaceAll("-", ""));
        //默认设备为全天报警
        infoPo.setAlwaysAlarm("1");
        int insert = hospitalRegistrationInfoDao.insert(infoPo);
        if (insert <= 0) {
            throw  new IedsException(HospitalEnumErrorCode.ADD_HOSPITAL_INFO_FAILED.getCode());
        }
    }

}
