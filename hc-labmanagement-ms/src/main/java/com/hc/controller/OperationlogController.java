package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.OperationlogApplication;
import com.hc.application.command.OperationLogCommand;
import com.hc.command.labmanagement.operation.ExportLogCommand;
import com.hc.command.labmanagement.operation.HospitalEquipmentOperationLogCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.my.common.core.jwt.JwtIgnore;
import com.hc.vo.backlog.OperationlogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


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


    @JwtIgnore
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

    @JwtIgnore
    @PostMapping("/addUserRightLogInfo")
    @ApiOperation("新增用户")
     public void addUserRightLog(@RequestBody UserRightInfoCommand userRightInfoCommand){
        operationlogApplication.addUserRightLog(userRightInfoCommand);
     }

     @PostMapping("/findAllLogInfo")
     @ApiOperation("分页获取日志信息")
    public Page<OperationlogVo> getAllLogInfo(@RequestBody OperationLogCommand operationLogCommand){
        return operationlogApplication.findAllLogInfo(operationLogCommand);
    }

    @JwtIgnore
    @PostMapping("/addExportLog")
    @ApiOperation("新增导出日志")
    public void addExportLog(@RequestBody ExportLogCommand exportLogCommand){
        operationlogApplication.addExportLog(exportLogCommand);
    }

    @PostMapping("/exportLogInfo")
    @ApiOperation("导出系统日志")
    public void exportLogInfo(@RequestBody OperationLogCommand operationLogCommand, HttpServletResponse response){
        operationlogApplication.exportLogInfo(operationLogCommand,response);
    }
}
