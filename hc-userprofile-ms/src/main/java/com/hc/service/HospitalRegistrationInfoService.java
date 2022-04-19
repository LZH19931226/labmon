package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;

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
    List<HospitalRegistrationInfoDto> selectHospitalInfo(Page page, HospitalCommand hospitalCommand);

    /**
     * 插入医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    void insertHospitalInfo(HospitalCommand hospitalCommand);

    /**
     * 修改医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    void editHospitalInfo(HospitalCommand hospitalCommand);

    /**
     * 更具医院编码删除医院信息
     * @param hospitalCode 医院编码
     */
    void deleteHospitalInfoByCode(String hospitalCode);

    /**
     * 获取医院名称列表
     * @return 医院名称集合
     */
    List<HospitalRegistrationInfoDto> selectHospitalNameList();

}
