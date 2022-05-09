package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.application.command.OperationLogCommand;
import com.hc.dto.OperationlogDTO;
import com.hc.po.OperationlogPo;
import com.hc.vo.backlog.OperationlogVo;

import java.util.List;


public interface OperationlogRepository extends IService <OperationlogPo>{


    /**
     * 分页获取全部日志信息
     * @param page
     * @param operationLogCommand
     * @return
     */
    List<OperationlogDTO> findAllLogInfo(Page<OperationlogVo> page, OperationLogCommand operationLogCommand);
}