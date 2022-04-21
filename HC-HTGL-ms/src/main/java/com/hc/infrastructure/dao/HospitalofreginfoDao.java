package com.hc.infrastructure.dao;

import com.hc.entity.Hospitalofreginfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-05.
 */
@Transactional
public interface HospitalofreginfoDao extends JpaRepository<Hospitalofreginfo,String> {


}

