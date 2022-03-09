package com.hc.dao;

import com.hc.entity.MonitorEquipmentWarningTime;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional
public interface MonitorEquipmentWarningTimeDao extends JpaRepository<MonitorEquipmentWarningTime,Integer> {

    int deleteByEquipmentid(String equipmentid);
}
