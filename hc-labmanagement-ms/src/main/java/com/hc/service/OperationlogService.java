package com.hc.service;


import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.command.labmanagement.operation.MonitorEquipmentLogInfoCommand;

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
}

