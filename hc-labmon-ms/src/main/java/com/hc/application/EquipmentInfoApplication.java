package com.hc.application;

import com.hc.service.EquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipmentInfoApplication {

    @Autowired
    private EquipmentInfoService equipmentInfoService;
    
}
