package com.hc.dao;

import com.hc.entity.Monitordoorstaterecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 16956 on 2018-08-08.
 */
public interface MonitroDoorDao extends JpaRepository<Monitordoorstaterecord,String> {
}
