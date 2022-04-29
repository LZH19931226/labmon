package com.hc.service;


import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;

/**
 * 系统操作日志表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface OperationlogService{


    void addHospitalOperationlog(HospitalOperationLogCommand hospitalOperationLogCommand);
}

