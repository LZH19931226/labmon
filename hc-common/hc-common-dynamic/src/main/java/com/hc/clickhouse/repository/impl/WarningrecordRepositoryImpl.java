package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.WarningrecordMapper;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@DS("slave")
@Repository
public class WarningrecordRepositoryImpl extends ServiceImpl<WarningrecordMapper, Warningrecord> implements WarningrecordRepository {

    @Autowired
    private WarningrecordMapper warningrecordMapper;

    @Override
    public IPage<Warningrecord> getWarningRecord(Page<Warningrecord> page) {
        return page(page, Wrappers.lambdaQuery(new Warningrecord()).orderByDesc(Warningrecord::getInputdatetime));
    }

    @Override
    public void updateIsPhoneInfo(String pkid, String isPhone) {
        warningrecordMapper.updateIsPhoneInfo(pkid,isPhone);
    }

    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<Warningrecord> getWarningRecordInfo(String equipmentNo) {
        return list(Wrappers.lambdaQuery(new Warningrecord()).eq(Warningrecord::getEquipmentno,equipmentNo).eq(Warningrecord::getIsphone,"1"));
    }
}
