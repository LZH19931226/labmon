package com.hc.dao;

import com.hc.entity.Monitorinstrument;
import com.hc.entity.Sendrecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 15350 on 2020/2/8.
 */
public interface SendrecordDao extends JpaRepository<Sendrecord,String> {
}
