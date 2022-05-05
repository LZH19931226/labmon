package com.hc.infrastructure.dao;

import com.hc.po.Monitorinstrument;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Transactional
public interface MonitorInstrumentDao extends JpaRepository<Monitorinstrument,String> {


    List<Monitorinstrument> getByEquipmentno(String equipmentno);

}
