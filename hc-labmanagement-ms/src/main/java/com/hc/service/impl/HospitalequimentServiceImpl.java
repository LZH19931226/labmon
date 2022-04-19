package com.hc.service.impl;

import com.hc.application.command.HospitalEquimentTypeCommand;
import com.hc.repository.HospitalequimentRepository;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import com.hc.service.HospitalequimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalequimentServiceImpl implements HospitalequimentService {


    @Autowired
    private HospitalequimentRepository hospitalequimentRepository;

    @Autowired
    private MonitorequipmentwarningtimeRepository monitorequipmentwarningtimeRepository;

    @Override
    public void addHospitalEquimentType(HospitalEquimentTypeCommand hospitalEquimentTypeCommand) {

    }
}