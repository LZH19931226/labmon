package com.hc.dao;

import com.hc.entity.Hospitalequiment;
import com.hc.entity.HospitalequimentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-06.
 */
@Transactional
public interface HospitalEquipmentDao extends JpaRepository<Hospitalequiment,HospitalequimentKey> {
    /**
     * 根据医院编号删除设备类型信息
     * @param
     * @return
     */
    @Modifying
    @Query("delete from Hospitalequiment  hospitalEquipment where hospitalEquipment.hospitalequimentKey=:hospitalequimentKey ")
    Integer deleteByEquipmenttypeid(@Param("hospitalequimentKey") HospitalequimentKey hospitalequimentKey);

    /**
     * 将当前设备类型修改为不可用
     */
    @Modifying
    @Query("update Hospitalequiment  a set a.isvisible=:isvisible,a.timeout =:timeout,a.timeouttime=:timeouttime,a.alwayalarm=:alwayalarm" +
            " where a.hospitalequimentKey=:hospitalequimentKey ")
    Integer upateByEquipmenttypeid(@Param("isvisible") String isvisible,
                                   @Param("timeout") String timeout,
                                   @Param("timeouttime") Integer timeouttime,
                                   @Param("alwayalarm") String alwayalarm,
                                   @Param("hospitalequimentKey") HospitalequimentKey hospitalequimentKey);

}
