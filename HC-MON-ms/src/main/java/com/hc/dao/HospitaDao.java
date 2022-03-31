package com.hc.mapper;

import com.hc.entity.Hospitalofreginfo;
import com.hc.entity.IosFileEntity;
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
