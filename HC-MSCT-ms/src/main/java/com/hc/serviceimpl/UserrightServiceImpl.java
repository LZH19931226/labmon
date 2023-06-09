package com.hc.serviceimpl;

import com.hc.mapper.UserrightDao;
import com.hc.po.Userright;
import com.hc.service.UserrightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserrightServiceImpl implements UserrightService {

    @Autowired
    private UserrightDao userrightDao;

    @Override
    public List<Userright> findALLUserRightInfoByHC(String hospitalcode) {
        return userrightDao.findALLUserRightInfoByHC(hospitalcode);
    }
}
