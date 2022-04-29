package com.hc.controller;

import com.hc.application.OperationlogApplication;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




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


}
