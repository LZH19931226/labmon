package com.hc.infrastructure.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.dto.LabMessengerPublishTaskDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LabMessengerPublishTaskDao extends BaseMapper<LabMessengerPublishTaskDto> {

    List<LabMessengerPublishTaskDto> getAlarmNoticeInfo(Page page, @Param("res") AlarmNoticeCommand alarmNoticeCommand);
}
