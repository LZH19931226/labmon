package com.hc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hc.dto.LabHosWarningTimeDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.LabHosWarningTimePo;
import com.hc.repository.LabHosWarningTimeRepository;
import com.hc.service.LabHosWarningTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabHosWarningTimeServiceImpl implements LabHosWarningTimeService {

    @Autowired
    private LabHosWarningTimeRepository labHosWarningTimeRepository;

    @Override
    public void saveObj(List<LabHosWarningTimeDto> labHosWarningTimes) {
        List<LabHosWarningTimePo> convert = BeanConverter.convert(labHosWarningTimes, LabHosWarningTimePo.class);
        labHosWarningTimeRepository.saveBatch(convert);
    }

    @Override
    public void removeObjByCode(String hospitalCode) {
        labHosWarningTimeRepository.remove(Wrappers.lambdaQuery(new LabHosWarningTimePo()).eq(LabHosWarningTimePo::getHospitalCode,hospitalCode));
    }

    @Override
    public List<LabHosWarningTimeDto> getAll() {
        List<LabHosWarningTimePo> list = labHosWarningTimeRepository.list();
        return BeanConverter.convert(list,LabHosWarningTimeDto.class);
    }
}
