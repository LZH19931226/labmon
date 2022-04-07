package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.WarningrecordDto;
import com.hc.po.WarningrecordPo;

import java.util.List;

public interface WarningrecordRepository extends IService<WarningrecordPo> {
    List<WarningrecordDto> getWarningRecord(String hospitalcode);
}
