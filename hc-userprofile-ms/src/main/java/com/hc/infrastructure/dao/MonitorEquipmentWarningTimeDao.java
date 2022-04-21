package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.MonitorEquipmentWarningTimePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 监控设备预警时间
 * @author hc
 */
@Mapper
public interface MonitorEquipmentWarningTimeDao extends BaseMapper<MonitorEquipmentWarningTimePo> {
}
