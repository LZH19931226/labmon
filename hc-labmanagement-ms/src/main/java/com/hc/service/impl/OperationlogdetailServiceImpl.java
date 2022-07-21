package com.hc.service.impl;

import com.hc.dto.OperationlogdetailDTO;
import com.hc.repository.OperationlogdetailRepository;
import com.hc.service.OperationlogdetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationlogdetailServiceImpl implements OperationlogdetailService {

    @Autowired
    private OperationlogdetailRepository operationlogdetailRepository;
    /**
     * 通过id获取日志信息
     *
     * @param logId
     * @return
     */
    @Override
    public List<OperationlogdetailDTO> getDetailedLogById(String logId) {
        return operationlogdetailRepository.getDetailedLogById(logId);
    }
}
