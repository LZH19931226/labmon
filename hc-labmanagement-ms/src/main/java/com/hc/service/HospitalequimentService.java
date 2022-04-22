package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.dto.HospitalequimentDTO;

import java.util.List;

/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface HospitalequimentService{


    void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand);

    void updateHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand);

    List<HospitalequimentDTO> selectHospitalEquimentType(Page page,HospitalEquimentTypeCommand hospitalEquimentTypeCommand);

    void deleteHospitalEquimentType(String hospitalCode, String equipmenttypeid);
}

