package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.command.labmanagement.model.hospital.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.infrastructure.dao.HospitalEquipmentDao;
import com.hc.infrastructure.dao.HospitalRegistrationInfoDao;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.HospitalEquipmentPo;
import com.hc.po.HospitalRegistrationInfoPo;
import com.hc.repository.HospitalRegistrationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

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
    public List<HospitalRegistrationInfoDto> selectHospitalInfo(Page page, HospitalCommand hospitalCommand) {
        return hospitalRegistrationInfoDao.selectListByHospital(page,hospitalCommand.getHospitalName(),hospitalCommand.getIsEnable());
    }

    /**
     * 插入医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    @Override
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        HospitalRegistrationInfoPo infoPo = BeanConverter.convert(hospitalCommand, HospitalRegistrationInfoPo.class);
        HospitalRegistrationInfoPo selectOne = hospitalRegistrationInfoDao.selectOne(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalName, hospitalCommand.getHospitalName()));
        if(null!=selectOne){
            throw new IedsException(LabSystemEnum.HOSPITAL_FULL_NAME_ALREADY_EXISTS.getMessage());
        }
       hospitalRegistrationInfoDao.insert(infoPo);

    }

    /**
     * 修改医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    @Override
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        HospitalEquipmentPo hospitalEquipmentPo =
                hospitalRegistrationInfoDao.selectHospitalName(hospitalCommand.getHospitalName(),hospitalCommand.getHospitalCode());
        if (!ObjectUtils.isEmpty(hospitalEquipmentPo)){
            throw new IedsException(LabSystemEnum.HOSPITAL_NAME_ALREADY_EXISTS.getMessage());
        }
        HospitalRegistrationInfoPo convert = BeanConverter.convert(hospitalCommand, HospitalRegistrationInfoPo.class);
        convert.setUpdateTime(new Date());
        hospitalRegistrationInfoDao.updateById(convert);
    }

    /**
     * 更据医院编码删除医院信息
     * @param hospitalCode 医院编码
     */
    @Override
    public void deleteHospitalInfoByCode(String hospitalCode) {
        //判断医院有没有绑定的设备
        Integer integer = hospitalEquipmentDao.selectCount(Wrappers.lambdaQuery(new HospitalEquipmentPo())
                .eq(HospitalEquipmentPo::getHospitalCode, hospitalCode));
        if(integer>0){
            throw new IedsException(LabSystemEnum.HOSPITAL_INFO_NOTABLE_DELETED.getMessage());
        }
        int delete = hospitalRegistrationInfoDao.delete(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalCode, hospitalCode));
        if(delete<=0){
            throw new IedsException(LabSystemEnum.HOSPITAL_INFO_DELETE_FAIL.getMessage());
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
     * 查询医院信息
     * @param hospitalCode
     * @return
     */
    @Override
    public HospitalRegistrationInfoDto findHospitalInfoByCode(String hospitalCode) {
        HospitalRegistrationInfoPo hospitalRegistrationInfoPo = hospitalRegistrationInfoDao.selectOne(Wrappers.lambdaQuery(new HospitalRegistrationInfoPo())
                .eq(HospitalRegistrationInfoPo::getHospitalCode, hospitalCode));
        return BeanConverter.convert(hospitalRegistrationInfoPo,HospitalRegistrationInfoDto.class);
    }

    /**
     * 获取医院code集合
     *
     * @return
     */
    @Override
    public List<String> selectHospitalCodeList() {
        return hospitalRegistrationInfoDao.selectHospitalCodeList();
    }

    /**
     * 获取所有的医院信息
     * @return
     */
    @Override
    public List<HospitalRegistrationInfoDto> getAllHospitalInfo() {
        List<HospitalRegistrationInfoPo> hospitalRegistrationInfoPos = hospitalRegistrationInfoDao.selectList(null);
        return BeanConverter.convert(hospitalRegistrationInfoPos,HospitalRegistrationInfoDto.class);
    }
}
