package com.hc.service;


import com.hc.application.command.HospitalEquimentTypeCommand;

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
}

