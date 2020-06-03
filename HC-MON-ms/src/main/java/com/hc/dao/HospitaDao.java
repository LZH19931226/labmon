package com.hc.dao;

import com.hc.entity.Hospitalofreginfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 16956 on 2018-08-30.
 */
public interface HospitaDao extends JpaRepository<Hospitalofreginfo,String> {
}
