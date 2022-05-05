package com.hc.service;

import com.hc.po.Operationlog;
import com.hc.po.Operationlogdetail;

import java.util.List;

/**
 * Created by 15350 on 2020/5/20.
 */
public interface OperationlogService {

    /**
     * 新增操作日志信息
     */
    void addOperationLogInfo(Operationlog operationlog, List<Operationlogdetail> operationlogdetails);



}
