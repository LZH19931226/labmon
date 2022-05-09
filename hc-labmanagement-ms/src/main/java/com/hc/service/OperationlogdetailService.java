package com.hc.service;


import com.hc.dto.OperationlogdetailDTO;

import java.util.List;

/**
 * 系统操作日志详细表
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface OperationlogdetailService{

    /**
     * 通过id获取日志信息
     * @param logId
     * @return
     */
    List<OperationlogdetailDTO> getDetailedLogById(String logId);
}

