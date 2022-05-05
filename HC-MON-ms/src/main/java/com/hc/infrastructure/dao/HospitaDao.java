package com.hc.infrastructure.dao;

import com.hc.po.Hospitalofreginfo;
import com.hc.po.IosFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 16956 on 2018-08-30.
 */
public interface HospitaDao extends JpaRepository<Hospitalofreginfo,String> {
    @Query(value = "select * from hospitalofreginfo where hospitalcode = :hospitalcode",nativeQuery = true)
    Hospitalofreginfo findOne(@Param("hospitalcode") String hospitalcode);
}
