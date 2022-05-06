package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.command.labmanagement.hospital.HospitalCommand;
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
     * 插入医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    void insertHospitalInfo(HospitalCommand hospitalCommand);

    /**
     *  修改医院信息
     * @param hospitalCommand 医院信息数据传输对象
     */
    void editHospitalInfo(HospitalCommand hospitalCommand);

    /**
     * 更据医院编码删除医院信息
     * @param hospitalCode 医院编码
     */
    void deleteHospitalInfoByCode(String hospitalCode);

    /**
     * 获取医院名称列表
     * @return 医院名称集合
     */
    List<HospitalRegistrationInfoDto> selectHospitalNameList();

    /**
     * 根据医院名称查询医院信息
     * @param hospitalName
     * @return
     */
    HospitalRegistrationInfoDto selectHospitalInfoByHospitalName(String hospitalName);

    /**
     *
     * @param hospitalCode
     * @return
     */
    HospitalRegistrationInfoDto findHospitalInfoByCode(String hospitalCode);
}
