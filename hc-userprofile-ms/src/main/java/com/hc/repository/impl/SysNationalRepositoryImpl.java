package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.SysNationalDto;
import com.hc.infrastructure.dao.SysNationalDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.SysNationalPo;
import com.hc.repository.SysNationalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 国家信息库实现
 * @author hc
 */
@Repository
public class SysNationalRepositoryImpl extends ServiceImpl<SysNationalDao, SysNationalPo> implements SysNationalRepository {

    @Autowired
    private SysNationalDao sysNationalDao;

    @Override
    public List<SysNationalDto> getNational() {
        List<SysNationalPo> sysNationalPoList = sysNationalDao.selectList(Wrappers.lambdaQuery(new SysNationalPo()));
        List<SysNationalDto> sysNationalDtoList = BeanConverter.convert(sysNationalPoList, SysNationalDto.class);
        return sysNationalDtoList;
    }

}
