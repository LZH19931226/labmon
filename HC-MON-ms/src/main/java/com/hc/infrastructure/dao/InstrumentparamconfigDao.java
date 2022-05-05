package com.hc.infrastructure.dao;

import com.hc.po.Instrumentparamconfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-05.
 */
@Transactional
public interface InstrumentparamconfigDao extends JpaRepository<Instrumentparamconfig,String> {
    /**
     * 更改当前报警探头监控类型状态
     * @param instrumentparamconfig
     * @return
     */
    @Modifying
    @Query("update Instrumentparamconfig instrumentparamconfig set instrumentparamconfig.pushtime=:pushtime, " +
            "instrumentparamconfig.warningphone=:warningphone where instrumentparamconfig.instrumentparamconfigno =:instrumentparamconfigno")
    Integer updateWarningState(Instrumentparamconfig instrumentparamconfig);



}
