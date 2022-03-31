package com.hc.mapper;

import com.hc.entity.Instrumentparamconfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-06.
 */
@Transactional
public interface InstrumentParamConfigDao extends JpaRepository<Instrumentparamconfig,String> {

    @Modifying
    @Query("delete from Instrumentparamconfig a where a.instrumentno = :instrumentno")
    Integer deleteByInstrumentno(@Param("instrumentno") String instrumentno);

}
