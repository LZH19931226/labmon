package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.AlarmNoticeCommand;
import com.hc.dto.LabMessengerPublishTaskDto;
import com.hc.infrastructure.dao.LabMessengerPublishTaskDao;
import com.hc.repository.LabMessengerPublishTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LabMessengerPublishTaskRepositoryImpl extends ServiceImpl<LabMessengerPublishTaskDao, LabMessengerPublishTaskDto> implements LabMessengerPublishTaskRepository {

    @Autowired
    private LabMessengerPublishTaskDao labMessengerPublishTaskDao;

    @Override
    public List<LabMessengerPublishTaskDto> getAlarmNoticeInfo(Page page, AlarmNoticeCommand alarmNoticeCommand) {
        return labMessengerPublishTaskDao.getAlarmNoticeInfo(page,alarmNoticeCommand);
    }
}
