package com.hc.application;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.WarningInfoCommand;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarningInfoApplication {

    @Autowired
    private WarningrecordRepository warningrecordRepository;

    /**
     * 分页获取报警信息
     * @param warningInfoCommand
     * @return
     */
    public  Page<Warningrecord> getWarningRecord(WarningInfoCommand warningInfoCommand) {
        Page<Warningrecord> page = new Page<>(warningInfoCommand.getPageCurrent(),warningInfoCommand.getPageSize());
        IPage<Warningrecord> warningRecordIPage = warningrecordRepository.getWarningRecord(page);
        List<Warningrecord> records = warningRecordIPage.getRecords();
        page.setRecords(records);
        return page;
    }
}
