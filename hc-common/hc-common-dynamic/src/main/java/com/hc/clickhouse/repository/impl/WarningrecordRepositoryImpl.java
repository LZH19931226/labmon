package com.hc.clickhouse.repository.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.mapper.WarningrecordMapper;
import com.hc.clickhouse.param.EquipmentDataParam;
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
    public IPage<Warningrecord> getWarningRecord(Page<Warningrecord> page,String hospitalCode) {
        return page(page, Wrappers.lambdaQuery(new Warningrecord()).eq(Warningrecord::getHospitalcode,hospitalCode).orderByDesc(Warningrecord::getInputdatetime));
    }


    /**
     * @param equipmentNo
     * @return
     */
    @Override
    public List<Warningrecord> getWarningRecordInfo(String equipmentNo) {
        return list(Wrappers.lambdaQuery(new Warningrecord()).eq(Warningrecord::getEquipmentno, equipmentNo));
    }

    /**
     * @param equipmentNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<Warningrecord> getWarningRecordDetailInfo(Page page ,String equipmentNo, String startTime, String endTime) {
        return warningrecordMapper.getWarningRecordDetailInfo(page,equipmentNo, startTime, endTime);
    }

    /**
     * @param warningrecord
     */
    @Override
    public void saveWarningInfo(Warningrecord warningrecord) {
        warningrecordMapper.insert(warningrecord);
    }

    @Override
    public void updateWarningCallUser(Warningrecord warningrecord) {
        LambdaUpdateWrapper<Warningrecord> warningrecordLambdaUpdateWrapper = Wrappers.lambdaUpdate();
        warningrecordLambdaUpdateWrapper.eq(Warningrecord::getPkid, warningrecord.getPkid());
        warningrecordLambdaUpdateWrapper.set(Warningrecord::getPhoneCallUser, warningrecord.getPhoneCallUser());
        warningrecordLambdaUpdateWrapper.set(Warningrecord::getMailCallUser, warningrecord.getMailCallUser());
        this.update(warningrecordLambdaUpdateWrapper);
    }

    @Override
    public List<Warningrecord> getWarningInfoByTime(String time) {
        return warningrecordMapper.getWarningInfoByTime(time);
    }

    @Override
    public List<Warningrecord> getSummaryOfAlarms(EquipmentDataParam convert) {
        return warningrecordMapper.getSummaryOfAlarms(convert);
    }

    @Override
    public List<Warningrecord> getWarningEquuipmentInfos(String hospitalCode, String startTime, String endTime) {
        return warningrecordMapper.getWarningEquuipmentInfos(hospitalCode, startTime, endTime);
    }

    @Override
    public List<Warningrecord> getWarningInfoByCode(String hospitalCode, Integer count) {
        return warningrecordMapper.getWarningEquuipmentCounts(hospitalCode,count);
    }

    @Override
    public List<Warningrecord> getAlarmDeviceNum(EquipmentDataParam convert) {
        return warningrecordMapper.getAlarmDeviceNum(convert);
    }

    @Override
    public List<Warningrecord> getWarningInfoList(String hospitalCode, String ymd) {
        return warningrecordMapper.getWarningInfoList(hospitalCode,ymd);
    }

    @Override
    public void batchInsert(List<Warningrecord> convert) {
        warningrecordMapper.insertBatchSomeColumn(convert);
    }
}
