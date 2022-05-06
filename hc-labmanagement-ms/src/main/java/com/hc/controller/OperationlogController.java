package com.hc.controller;

import com.hc.application.OperationlogApplication;
import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * 系统操作日志表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@RestController
@Api(tags = "系统操作日志表")
@RequestMapping("/operationlog")
public class OperationlogController {

    @Autowired
    private OperationlogApplication operationlogApplication;


    @PostMapping("/addHospitalOperationlog")
    @ApiOperation("新增医院管理操作日志")
    public void addHospitalOperationlog(@RequestBody HospitalOperationLogCommand hospitalOperationLogCommand){
        operationlogApplication.addHospitalOperationlog(hospitalOperationLogCommand);
    }

    @PostMapping("/addHospitalEquimentType")
    @ApiOperation("新增医院设备类型")
    public void addHospitalEquimentType(@RequestBody HospitalEquipmentOperationLogCommand hospitalEquipmentOperationLogCommand){
        operationlogApplication.addHospitalEquipmentOperationLogCommand(hospitalEquipmentOperationLogCommand);
    }


}
