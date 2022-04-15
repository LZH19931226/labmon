package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.vo.hospital.HospitalInfoVo;

import java.util.List;

/**
 * 医院注册信息服务层
 * @author hc
 */
public interface HospitalRegistrationInfoService {

    /**
     * 查询医院信息
     * @param page 分页对象
     * @param hospitalCommand 医院信息数据传输对象
     * @return
     */
    List<HospitalRegistrationInfoDto> selectHospitalInfo(Page<HospitalInfoVo> page, HospitalCommand hospitalCommand);

    /**
     * 插入医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    void insertHospitalInfo(HospitalCommand hospitalCommand);

}
