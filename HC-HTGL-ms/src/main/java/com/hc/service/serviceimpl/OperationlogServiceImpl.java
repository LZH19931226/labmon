package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.infrastructure.dao.OperationlogDao;
import com.hc.infrastructure.dao.OperationlogdetailDao;
import com.hc.entity.Operationlog;
import com.hc.entity.Operationlogdetail;
import com.hc.mapper.laboratoryFrom.OperationlogdetaiMapper;
import com.hc.service.OperationlogService;
import com.hc.units.ApiResponse;
import org.apache.commons.collections4.CollectionUtils;
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

    @Override
    public ApiResponse<Page<Operationlog>> getAllOperationLog(Operationlog operationlog) {

        Integer pagenum = operationlog.getPagenum();
        Integer pagesize = operationlog.getPagesize();
        Integer start = (pagenum - 1) * pagesize;
        Integer end = pagesize;
        PageRowBounds rowBounds = new PageRowBounds(start, end);
        List<Operationlog> allOperationLogInfo = operationlogdetaiMapper.getAllOperationLogInfo(rowBounds, operationlog);

        ApiResponse<Page<Operationlog>> apiResponse = new ApiResponse<>();
        if (CollectionUtils.isEmpty(allOperationLogInfo)) {
            apiResponse.setCode(ApiResponse.NOT_FOUND);
            apiResponse.setMessage("无信息");
            return apiResponse;
        }
        PageInfo<Operationlog> pageInfo = new PageInfo<Operationlog>(allOperationLogInfo);

        apiResponse.setPage(pageInfo);
        return apiResponse;
    }

    @Override
    public ApiResponse<List<Operationlogdetail>> getOperationlogDetailInfo(String logid) {
        List<Operationlogdetail> operationLogdeatil = operationlogdetaiMapper.getOperationLogdeatil(logid);
        ApiResponse<List<Operationlogdetail>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(operationLogdeatil);

        return apiResponse;
    }
}
