package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.command.labmanagement.hospital.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.infrastructure.dao.HospitalEquipmentDao;
import com.hc.infrastructure.dao.HospitalRegistrationInfoDao;
import com.hc.my.common.core.constant.enums.HospitalEnumErrorCode;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalEquipmentPo;
import com.hc.po.HospitalRegistrationInfoPo;
import com.hc.repository.HospitalRegistrationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    @Autowired
    private HospitalEquipmentDao hospitalEquipmentDao;

    /**
     * 查询医院信息
     * @param page 分页对象
     * @param hospitalCommand 医院信息数据传输对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HospitalRegistrationInfoDto> selectHospitalInfo(Page page, HospitalCommand hospitalCommand) {
        return hospitalRegistrationInfoDao.selectListByHospital(page,hospitalCommand.getHospitalName(),hospitalCommand.getIsEnable());
    }

    /**
     * 插入医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        HospitalRegistrationInfoPo infoPo = BeanConverter.convert(hospitalCommand, HospitalRegistrationInfoPo.class);
        HospitalRegistrationInfoPo selectOne = hospitalRegistrationInfoDao.selectOne(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalName, hospitalCommand.getHospitalName()));
        if(null!=selectOne){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_FULL_NAME_ALREADY_EXISTS.getCode());
        }
                 //设置操作时间
        infoPo.setUpdateTime(new Date())
                //设置医院的UUID
                .setHospitalCode(UUID.randomUUID().toString().replaceAll("-", ""))
                //设置是否启用
                .setIsEnable(hospitalCommand.getIsEnable())
                //默认设备为全天报警
                .setAlwaysAlarm("1");
       hospitalRegistrationInfoDao.insert(infoPo);

    }

    /**
     * 修改医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        HospitalEquipmentPo hospitalEquipmentPo =
                hospitalRegistrationInfoDao.selectHospitalName(hospitalCommand.getHospitalName(),hospitalCommand.getHospitalCode());
        if (!ObjectUtils.isEmpty(hospitalEquipmentPo)){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_NAME_ALREADY_EXISTS.getCode());
        }
        HospitalRegistrationInfoPo convert = BeanConverter.convert(hospitalCommand, HospitalRegistrationInfoPo.class);
        hospitalRegistrationInfoDao.updateById(convert);
    }

    /**
     * 更据医院编码删除医院信息
     * @param hospitalCode 医院编码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHospitalInfoByCode(String hospitalCode) {
        //判断医院有没有绑定的设备
        Integer integer = hospitalEquipmentDao.selectCount(Wrappers.lambdaQuery(new HospitalEquipmentPo())
                .eq(HospitalEquipmentPo::getHospitalCode, hospitalCode));
        if(integer>0){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_INFO_NOTABLE_DELETED.getCode());
        }
        int delete = hospitalRegistrationInfoDao.delete(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalCode, hospitalCode));
        if(delete<=0){
            throw new IedsException(HospitalEnumErrorCode.HOSPITAL_INFO_DELETE_FAIL.getCode());
        }
    }

    /**
     * 获取医院名称列表
     * @return 医院名称集合
     */
    @Override
    public List<HospitalRegistrationInfoDto> selectHospitalNameList() {
        List<HospitalRegistrationInfoPo> list
                = hospitalRegistrationInfoDao.selectList(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo()));
        return list != null && list.size() !=0 ?
                BeanConverter.convert(list,HospitalRegistrationInfoDto.class):null;
    }

    /**
     * 根据医院名称查询医院信息
     *
     * @param hospitalName
     * @return
     */
    @Override
    public HospitalRegistrationInfoDto selectHospitalInfoByHospitalName(String hospitalName) {
        HospitalRegistrationInfoPo hospitalRegistrationInfoPo = hospitalRegistrationInfoDao.selectOne(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalName, hospitalName));
        return BeanConverter.convert(hospitalRegistrationInfoPo,HospitalRegistrationInfoDto.class);
    }

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public HospitalRegistrationInfoDto findHospitalInfoByCode(String hospitalCode) {
        HospitalRegistrationInfoPo hospitalRegistrationInfoPo = hospitalRegistrationInfoDao.selectOne(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalCode, hospitalCode));
        return BeanConverter.convert(hospitalRegistrationInfoPo,HospitalRegistrationInfoDto.class);
    }
}
