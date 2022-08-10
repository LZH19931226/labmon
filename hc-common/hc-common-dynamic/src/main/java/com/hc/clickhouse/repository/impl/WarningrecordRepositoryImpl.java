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


    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<Warningrecord> getWarningRecordInfo(String equipmentNo) {
        return list(Wrappers.lambdaQuery(new Warningrecord()).eq(Warningrecord::getEquipmentno,equipmentNo));
    }

    /**
     * @param hospitalCode
     * @return
     */
    @Override
    public List<Warningrecord> getWarningInfo(String hospitalCode,String startTime,String endTime) {
        return warningrecordMapper.getWarningInfo(hospitalCode,startTime,endTime);
    }

    /**
     * @param equipmentNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<Warningrecord> getWarningRecordDetailInfo(String equipmentNo, String startTime, String endTime) {
        return warningrecordMapper.getWarningRecordDetailInfo(equipmentNo,startTime,endTime);
    }

    /**
     * @param warningrecord
     */
    @Override
    public void saveWarningInfo(Warningrecord warningrecord) {
        warningrecordMapper.insert(warningrecord);
    }
}
