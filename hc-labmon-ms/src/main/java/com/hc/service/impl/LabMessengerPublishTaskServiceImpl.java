package com.hc.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.dto.LabMessengerPublishTaskDto;
import com.hc.repository.LabMessengerPublishTaskRepository;
import com.hc.service.LabMessengerPublishTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabMessengerPublishTaskServiceImpl implements LabMessengerPublishTaskService {
    @Autowired
    private LabMessengerPublishTaskRepository labMessengerPublishTaskRepository;

    @Override
    public List<LabMessengerPublishTaskDto> getAlarmNoticeInfo(Page page, AlarmNoticeCommand alarmNoticeCommand) {
        return labMessengerPublishTaskRepository.getAlarmNoticeInfo(page,alarmNoticeCommand);
    }
}
