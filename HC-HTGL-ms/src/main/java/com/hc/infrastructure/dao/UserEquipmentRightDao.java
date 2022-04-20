package com.hc.infrastructure.dao;

import com.hc.entity.Userequipmentright;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-06.
 */
@Transactional
public interface UserEquipmentRightDao extends JpaRepository<Userequipmentright,Integer> {
    @Modifying
    @Query("delete from Userequipmentright  userequipmentright where userequipmentright.userid=:userid")
    Integer deleteAllByUserid(@Param("userid") String userid);

    @Modifying
    @Query("delete from Userequipmentright  userequipmentright where userequipmentright.userid=:userid and userequipmentright.equipmentitem=:equipmentitem ")
    Integer deleteByEquipmentitem(@Param("userid") String userid,@Param("equipmentitem") String equipmentitem);
}

