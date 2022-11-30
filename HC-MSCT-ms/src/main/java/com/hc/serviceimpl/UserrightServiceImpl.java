package com.hc.serviceimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.mapper.UserrightDao;
import com.hc.my.common.core.constant.enums.DictEnum;
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
        return userrightDao.selectList(Wrappers.lambdaQuery(new Userright()).eq(Userright::getHospitalcode, hospitalcode).eq(Userright::getIsuse, DictEnum.TURN_ON.getCode()));
    }
}
