package com.hc.clickhouse.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.po.Warningrecord;

public interface WarningrecordRepository extends IService<Warningrecord> {

    IPage<Warningrecord> getWarningRecord(Page<Warningrecord> page);
}
