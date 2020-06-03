package com.hc.controller;

import com.github.pagehelper.Page;
import com.hc.entity.Operationlog;
import com.hc.entity.Operationlogdetail;
import com.hc.service.OperationlogService;
import com.hc.units.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 15350 on 2020/5/21.
 */
@Api(value = "日志信息查询", description = "日志信息查询Api")
@RestController
@RequestMapping(value = "/api/log", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OperationlogdetailMapper {

    @Autowired
    private OperationlogService operationlogService;

    @GetMapping("/getlogDeatil")
    public ApiResponse<List<Operationlogdetail>> getlogDeatil(@ApiParam(name = "logid", value = "模糊查询参数", required = true)
                                                                  @RequestParam(value = "logid", required = true) String logid){
        return operationlogService.getOperationlogDetailInfo(logid);
    }

    @GetMapping("/getAllLog")
    public ApiResponse<Page<Operationlog>> getAllLog(@ApiParam(name = "opeartiontype", value = "模糊查询参数", required = false)
                                                         @RequestParam(value = "opeartiontype", required = false) String opeartiontype,
                                                     @ApiParam(name = "pagesize", value = "每页显示条目数", required = true)
                                                         @RequestParam(value = "pagesize", required = true) Integer pagesize,
                                                     @ApiParam(name = "pagenum", value = "当前页码数", required = true)
                                                         @RequestParam(value = "pagenum", required = true) Integer pagenum,
                                                     @ApiParam(name = "username", value = "医院编码", required = false)
                                                         @RequestParam(value = "username", required = false) String username,
                                                     @ApiParam(name = "functionname", value = "设备类型编码", required = false)
                                                         @RequestParam(value = "functionname", required = false) String functionname,
                                                     @ApiParam(name = "begintime", value = "设备类型编码", required = false)
                                                         @RequestParam(value = "begintime", required = false) String begintime,
                                                     @ApiParam(name = "endtime", value = "设备类型编码", required = false)
                                                         @RequestParam(value = "endtime", required = false) String endtime,
                                                     @ApiParam(name = "hospitalname", value = "设备类型编码", required = false)
                                                         @RequestParam(value = "hospitalname", required = false) String hospitalname){
        Operationlog operationlog = new Operationlog();
        operationlog.setOpeartiontype(opeartiontype);
        operationlog.setPagenum(pagenum);
        operationlog.setPagesize(pagesize);
        operationlog.setUsername(username);
        operationlog.setFunctionname(functionname);
        operationlog.setBegintime(begintime);
        operationlog.setEndtime(endtime);
        operationlog.setHospitalname(hospitalname);
        return operationlogService.getAllOperationLog(operationlog);
    }

}
