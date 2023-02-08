package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.dto.LabMessengerPublishTaskDto;

import java.util.List;

public interface LabMessengerPublishTaskService {
    List<LabMessengerPublishTaskDto> getAlarmNoticeInfo(Page page, AlarmNoticeCommand alarmNoticeCommand);
}
