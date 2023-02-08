package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.dto.LabMessengerPublishTaskDto;

import java.util.List;

public interface LabMessengerPublishTaskRepository extends IService<LabMessengerPublishTaskDto> {
    List<LabMessengerPublishTaskDto> getAlarmNoticeInfo(Page page, AlarmNoticeCommand alarmNoticeCommand);
}
