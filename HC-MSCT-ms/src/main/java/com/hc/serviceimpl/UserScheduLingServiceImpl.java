package com.hc.serviceimpl;

import com.hc.mapper.UserScheduLingDao;
import com.hc.my.common.core.util.DateUtils;
import com.hc.po.UserScheduLing;
import com.hc.service.UserScheduLingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserScheduLingServiceImpl implements UserScheduLingService {

    @Autowired
    private UserScheduLingDao userScheduLingDao;

    @Override
    public List<UserScheduLing> getHospitalScheduleInfo(String hospitalcode) {
        Date date = new Date();
        String today = DateUtils.paseDate(date);
        return userScheduLingDao.getHospitalScheduleInfo(hospitalcode,today,DateUtils.getYesterday(date));
    }
}
