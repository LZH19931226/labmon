package com.hc.dao;

import com.hc.entity.Pressrecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 16956 on 2018-08-09.
 */
public interface PressDao extends JpaRepository<Pressrecord,String> {

}
