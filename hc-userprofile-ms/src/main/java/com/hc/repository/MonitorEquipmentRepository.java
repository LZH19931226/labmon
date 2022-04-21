package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.MonitorEquipmentCommand;
import com.hc.dto.MonitorEquipmentDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author hc
 */
@Repository
public interface MonitorEquipmentRepository {

    /**
     * 分页查询监控设备信息
     *
     * @param page                    分页对象
     * @param monitorEquipmentCommand 监控设备参数
     * @return
     */
    List<MonitorEquipmentDto> getEquipmentInfoList(Page page, MonitorEquipmentCommand monitorEquipmentCommand);
}
