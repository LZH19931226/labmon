package com.hc.controller;

import com.hc.application.OperationlogdetailApplication;
import com.hc.vo.backlog.OperationlogdetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 系统操作日志详细表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@RestController
@Api(tags = "系统操作日志详细表")
@RequestMapping("/operationlogdetail")
public class OperationlogdetailController {

    @Autowired
    private OperationlogdetailApplication operationlogdetailApplication;

    @GetMapping("/getlogDeatil")
    @ApiOperation("获取日志详情")
    public List<OperationlogdetailVo> getDetailedLog(@RequestParam String logId){
        return operationlogdetailApplication.getDetailedLogById(logId);
    }
}
