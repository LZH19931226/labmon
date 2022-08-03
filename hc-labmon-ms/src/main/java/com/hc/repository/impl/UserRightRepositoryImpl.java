package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.UserRightDto;
import com.hc.infrastructure.dao.UserRightDao;
import com.hc.repository.UserRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRightRepositoryImpl extends ServiceImpl<UserRightDao,UserRightDto> implements UserRightRepository {

    @Autowired
    private UserRightDao userRightDao;
}
