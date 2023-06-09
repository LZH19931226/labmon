package com.hc.clickhouse.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.param.AlarmDataParam;
import com.hc.clickhouse.param.EquipmentDataParam;
import com.hc.clickhouse.param.WarningRecordParam;
import com.hc.clickhouse.po.Warningrecord;

import java.util.List;

public interface WarningrecordRepository extends IService<Warningrecord> {

    IPage<Warningrecord> getWarningRecord(Page<Warningrecord> page, WarningRecordParam warningRecordParam);

    List<Warningrecord> getWarningRecordInfo(String equipmentNo);

    List<Warningrecord> getWarningRecordDetailInfo(Page page,String equipmentNo, String startTime, String endTime);

    void saveWarningInfo(Warningrecord warningrecord);

    void updateWarningCallUser(Warningrecord warningrecord);

    List<Warningrecord> getWarningInfoByTime(String time);

    List<Warningrecord> getSummaryOfAlarms(EquipmentDataParam convert);

    List<Warningrecord> getWarningEquuipmentInfos(String hospitalCode, String startTime, String endTime);

    List<Warningrecord> getWarningInfoByCode(String hospitalCode, Integer count);

    List<Warningrecord> getAlarmDeviceNum(EquipmentDataParam convert);

    List<Warningrecord> getWarningInfoList(String hospitalCode, String ymd);

    void batchInsert(List<Warningrecord> convert);

    Warningrecord getWarningInfo(String pkId, String ym);

    List<Warningrecord> getAlarmData(Page page, AlarmDataParam alarmDataParam);
}
