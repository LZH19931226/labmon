package com.hc.application;

import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.service.OperationlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 系统操作日志表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Component
public class OperationlogApplication {

    @Autowired
    private OperationlogService operationlogService;

    public void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand) {
        operationlogService.addHospitalOperationlog(hospitalOperationLogCommand);
    }

    public void addHospitalEquipmentOperationLogCommand(HospitalEquipmentOperationLogCommand hospitalEquipmentOperationLogCommand) {
        operationlogService.addHospitalEquipmentOperationLogCommand(hospitalEquipmentOperationLogCommand);
    }
}
