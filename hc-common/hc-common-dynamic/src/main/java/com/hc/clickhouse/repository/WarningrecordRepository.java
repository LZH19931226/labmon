package com.hc.clickhouse.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.po.Warningrecord;

import java.util.List;

public interface WarningrecordRepository extends IService<Warningrecord> {

    IPage<Warningrecord> getWarningRecord(Page<Warningrecord> page);

    List<Warningrecord> getWarningRecordInfo(String equipmentNo);

    List<Warningrecord> getWarningInfo(String hospitalCode,String startTime,String endTime);
}
