package com.hc.service.serviceimpl;

import com.hc.dao.OperationlogDao;
import com.hc.dao.OperationlogdetailDao;
import com.hc.entity.Operationlog;
import com.hc.entity.Operationlogdetail;
import com.hc.mapper.laboratoryFrom.OperationlogdetaiMapper;
import com.hc.service.OperationlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by 15350 on 2020/5/20.
 */
@Service
public class OperationlogServiceImpl implements OperationlogService {

    @Autowired
    private OperationlogDao operationlogDao;

    @Autowired
    private OperationlogdetailDao operationlogdetailDao;

    @Autowired
    private OperationlogdetaiMapper operationlogdetaiMapper;


    @Override
    public void addOperationLogInfo(Operationlog operationlog, List<Operationlogdetail> operationlogdetails) {
        // 执行日志表插入操作
        operationlog.setLogid(UUID.randomUUID().toString().replaceAll("-", ""));
        operationlogDao.saveAndFlush(operationlog);
        //执行日志信息表插入操作
        for (Operationlogdetail operationlogdetail : operationlogdetails) {
            operationlogdetail.setDetailid(UUID.randomUUID().toString().replaceAll("-", ""));
            operationlogdetail.setLogid(operationlog.getLogid());
            operationlogdetailDao.saveAndFlush(operationlogdetail);
        }
    }


}
