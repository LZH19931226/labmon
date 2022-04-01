package com.hc.dao;

import com.hc.entity.Userright;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Created by 16956 on 2018-08-06.
 */
@Transactional
public interface UserRightDao extends JpaRepository<Userright,String> {




}

