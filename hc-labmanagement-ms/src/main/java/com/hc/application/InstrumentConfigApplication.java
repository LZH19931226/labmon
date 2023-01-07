package com.hc.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentConfigCommand;
import com.hc.dto.InstrumentConfigDTO;
import com.hc.service.InstrumentConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class InstrumentConfigApplication {

    @Autowired
    private InstrumentConfigService instrumentConfigService;

    /**
     * 新增探头监测类型
     * @param instrumentConfigCommand 探头监测类型名称
     */
    public void save(InstrumentConfigCommand instrumentConfigCommand) {
        instrumentConfigService.save(instrumentConfigCommand);
    }

    /**
     * 分页查询监测类型
     * @param instrumentConfigCommand
     * @return
     */
    public Page<InstrumentConfigDTO> list(InstrumentConfigCommand instrumentConfigCommand) {
        Integer pageCurrent = instrumentConfigCommand.getPageCurrent();
        Integer pageSize = instrumentConfigCommand.getPageSize();
        Page<InstrumentConfigDTO> page = new Page<>(pageCurrent,pageSize);
        String instrumentConfigName = instrumentConfigCommand.getInstrumentConfigName();
        List<InstrumentConfigDTO> list = instrumentConfigService.listByPage(page,instrumentConfigName);
        page.setRecords(list);
        return page;
    }

    /**
     * 更新监测类型
     * @param instrumentConfigCommand
     */
    public void edit(InstrumentConfigCommand instrumentConfigCommand) {
        instrumentConfigService.edit(instrumentConfigCommand);
    }

    /**
     * 删除监测类型
     * @param instrumentConfigId
     */
    public void remove(String instrumentConfigId) {
        instrumentConfigService.remove(instrumentConfigId);
    }
}
