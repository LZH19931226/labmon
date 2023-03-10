package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.po.HospitalEquipmentPo;
import com.hc.po.HospitalRegistrationInfoPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 */
public interface HospitalRegistrationInfoDao extends BaseMapper<HospitalRegistrationInfoPo> {

    /**
     * 查询医院信息列表
     * @param page  分页对象
     * @param hospitalName 医院编码
     * @param isEnable 是否启用
     * @return 医院信息传输对象集合
     */
    List<HospitalRegistrationInfoDto> selectListByHospital(Page page,
                                                           @Param(value = "hospitalName") String hospitalName,
                                                           @Param(value = "isEnable") String isEnable);


    /**
     * 获取有无重复的医院名称
     * @param hospitalName 医院名称
     * @param hospitalCode 医院编码
     * @return 医院设备对象
     */
    HospitalEquipmentPo selectHospitalName(@Param("hospitalName") String hospitalName, @Param("hospitalCode") String hospitalCode);


    List<String> selectHospitalCodeList();
}
