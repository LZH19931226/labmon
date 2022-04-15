package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.HospitalCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.service.HospitalRegistrationInfoService;
import com.hc.vo.hospital.HospitalInfoVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 医院信息应用层
 * @author hc
 */
@Component
public class HospitalInfoApplication {

    @Autowired
    private HospitalRegistrationInfoService hospitalRegistrationInfoService;

    /**
     * 根据分页条件查询医院信息
     * @param hospitalCommand 医院传输对象
     * @param pageSize      分页大小
     * @param pageCurrent   当前页数
     * @return
     */
    public Page<HospitalInfoVo> selectHospitalInfo(HospitalCommand hospitalCommand, Long pageSize, Long pageCurrent) {
        Page<HospitalInfoVo> page = new Page<>(pageCurrent,pageSize);
        List<HospitalRegistrationInfoDto> hospitalInfos = hospitalRegistrationInfoService.selectHospitalInfo( page, hospitalCommand);
        List<HospitalInfoVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalInfos)) {
            hospitalInfos.forEach(res->{
                HospitalInfoVo build = HospitalInfoVo.builder()
                        .hospitalFullName(res.getHospitalFullName())
                        .hospitalName(res.getHospitalName())
                        .isEnable(res.getIsEnable())
                        .updateTime(res.getUpdateTime())
                        .build();
                list.add(build);
            });
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 插入医院信息
     * @param hospitalCommand 医院视图对象
     * @return
     */
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {

        hospitalRegistrationInfoService.insertHospitalInfo(hospitalCommand);

    }

    /**
     * 更新医院信息
     * @param hospitalCommand
     */
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        hospitalRegistrationInfoService.editHospitalInfo(hospitalCommand);
    }
}
