package com.hc.repository.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.infrastructure.dao.MonitorEquipmentWarningTimeDao;
import com.hc.po.MonitorEquipmentWarningTimePo;
import com.hc.repository.MonitorequipmentwarningtimeRepository;
import org.springframework.stereotype.Repository;


@Repository
public class MonitorequipmentwarningtimeRepositoryImpl extends ServiceImpl<MonitorEquipmentWarningTimeDao,MonitorEquipmentWarningTimePo> implements MonitorequipmentwarningtimeRepository  {


}