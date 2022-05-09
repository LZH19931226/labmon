package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.OperationlogdetailDTO;
import com.hc.po.OperationlogdetailPo;

import java.util.List;


public interface OperationlogdetailRepository extends IService <OperationlogdetailPo>{

    /**
     * 获取日志详细信息
     * @param logId
     * @return
     */
    List<OperationlogdetailDTO> getDetailedLogById(String logId);
}