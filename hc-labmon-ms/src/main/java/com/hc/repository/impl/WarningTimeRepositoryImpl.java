package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.MonitorEquipmentWarningTimeDTO;
import com.hc.infrastructure.dao.WarningTimeDao;
import com.hc.repository.WarningTimeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class WarningTimeRepositoryImpl extends ServiceImpl<WarningTimeDao, MonitorEquipmentWarningTimeDTO> implements WarningTimeRepository {

    private WarningTimeDao warningTimeDao;
}
