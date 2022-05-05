package com.hc.infrastructure.dao;

import com.hc.po.Monitorequipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-06.
 */
@Transactional
public interface MonitorEquipmentDao extends JpaRepository<Monitorequipment,String> {
    /**
     * 根据医院编号和设备编号删除设备
     * @param hospitalcode
     * @param equipmentno
     * @return
     */
    @Modifying
    @Query("delete from  Monitorequipment a where  a.equipmentno=:equipmentno")
    Integer deleteByHospitalcodeAndEquipmentno( @Param("equipmentno") String equipmentno);

}
