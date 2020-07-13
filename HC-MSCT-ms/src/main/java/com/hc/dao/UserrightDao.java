package com.hc.dao;

import com.hc.entity.Userright;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 15350 on 2019/10/9.
 */
@Component
public interface UserrightDao extends JpaRepository<Userright,String> {

    @Query("select  a from Userright a where a.hospitalcode=:hospitalcode and a.timeout = '1'")
    List<Userright> getUserrightByHospitalcodeAAndTimeout (@Param("hospitalcode") String hospitalcode);
}
