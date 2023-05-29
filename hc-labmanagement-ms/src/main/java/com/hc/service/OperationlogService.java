package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.AlarmSystemCommand;
import com.hc.application.command.OperationLogCommand;
import com.hc.command.labmanagement.operation.*;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.dto.OperationlogDTO;
import com.hc.vo.backlog.OperationlogVo;

import java.util.List;

/**
 * 系统操作日志表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface OperationlogService{


    void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand);

    void addHospitalEquipmentOperationLogCommand(HospitalEquipmentOperationLogCommand hospitalEquipmentOperationLogCommand);

    void addMonitorEquipmentLogInfo(MonitorEquipmentLogInfoCommand monitorEquipmentLogInfoCommand);

    void addInstrumentparamconfig(InstrumentParamConfigInfoCommand instrumentparamconfigInfoCommand);

    void addUserRightLog(UserRightInfoCommand userRightInfoCommand);

    /** 分页获取日志信息
     * @return
     * */
    List<OperationlogDTO> findAllLogInfo(Page<OperationlogVo> page, OperationLogCommand operationLogCommand);

    void addExportLog(ExportLogCommand exportLogCommand);

    void addAppLog(AlarmSystemCommand alarmSystemCommand);
}

