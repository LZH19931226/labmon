package com.hc.dao;

import com.hc.entity.Instrumentparamconfig;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 16956 on 2018-09-05.
 */
@Transactional
public interface InstrumentparamconfigDao extends JpaRepository<Instrumentparamconfig,String>{

    @Modifying
    @Query("update Instrumentparamconfig a set a.warningtime=:warningtime where a.instrumentparamconfigno=:instrumentparamconfigno")
    Integer updateWarnTime(@Param("warningtime")Date warningtime,@Param("instrumentparamconfigno") String instrumentparamconfigno);

    @Query(nativeQuery = true,value = "select ins.highlimit from Monitorinstrument mon left join Instrumentparamconfig ins " +
            "            on mon.instrumentno=ins.instrumentno where  mon.instrumentno=:instrumentno and ins.instrumentconfigid='14'")
    BigDecimal getMt200mHighLimit(@Param("instrumentno") String instrumentno);
}
