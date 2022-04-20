package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;
import com.hc.service.HospitalequimentService;
import com.hc.vo.equimenttype.HospitalequimentVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class HospitalequimentApplication {

    @Autowired
    private HospitalequimentService hospitalequimentService;


    public void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        hospitalequimentService.addHospitalEquimentType(hospitalEquimentTypeCommand);
    }

    public void updateHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        hospitalequimentService.updateHospitalEquimentType(hospitalEquimentTypeCommand);
    }

    public Page<HospitalequimentVo> selectHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {
        Page<HospitalequimentVo> page = new Page<>(hospitalEquimentTypeCommand.getPageCurrent(),hospitalEquimentTypeCommand.getPageSize());
        List<HospitalequimentVo> hospitalequimentVos = new ArrayList<>();
        List<HospitalequimentDTO> hospitalequimentDTOS = hospitalequimentService.selectHospitalEquimentType(hospitalEquimentTypeCommand);
        if (CollectionUtils.isNotEmpty(hospitalequimentDTOS)) {
            List<String> hospitalcodes = hospitalequimentDTOS.stream().map(HospitalequimentDTO::getHospitalcode).collect(Collectors.toList());

        }
        page.setRecords(hospitalequimentVos);
        return page;
    }

    public void deleteHospitalEquimentType(String hospitalCode, String equipmenttypeid) {


    }
}
