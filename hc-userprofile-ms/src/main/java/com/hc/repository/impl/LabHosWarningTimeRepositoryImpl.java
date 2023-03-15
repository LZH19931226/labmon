package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.infrastructure.dao.LabHosWarningTimeDao;
import com.hc.po.LabHosWarningTimePo;
import com.hc.repository.LabHosWarningTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LabHosWarningTimeRepositoryImpl extends ServiceImpl<LabHosWarningTimeDao, LabHosWarningTimePo> implements LabHosWarningTimeRepository {

    @Autowired
    private LabHosWarningTimeDao labHosWarningTimeDao;

}
