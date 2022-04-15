package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.po.HospitalRegistrationInfoPo;
import com.hc.vo.hospital.HospitalInfoVo;

import java.util.List;

/**
 *
 * @author user
 */

public interface HospitalRegistrationInfoRepository extends IService<HospitalRegistrationInfoPo> {
    /**
     * 查询医院信息
     * @param page 分页对象
     * @param hospitalCommand 医院信息数据传输对象
     * @return
     */
    List<HospitalRegistrationInfoDto> selectHospitalInfo(Page<HospitalInfoVo> page, HospitalCommand hospitalCommand);

    /**
     * 查润医院信息
     * @param hospitalCommand 医院信息数据传输对象
     * @return
     */
    void insertHospitalInfo(HospitalCommand hospitalCommand);

}
