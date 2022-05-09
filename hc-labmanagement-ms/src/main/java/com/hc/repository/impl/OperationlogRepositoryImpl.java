package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.application.command.OperationLogCommand;
import com.hc.dto.OperationlogDTO;
import com.hc.infrastructure.dao.OperationlogDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.OperationlogPo;
import com.hc.repository.OperationlogRepository;
import com.hc.vo.backlog.OperationlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class OperationlogRepositoryImpl extends ServiceImpl<OperationlogDao,OperationlogPo> implements OperationlogRepository  {

    @Autowired
    private  OperationlogDao operationlogDao;

    /**
     * 分页获取全部日志信息
     *
     * @param page
     * @param operationLogCommand
     * @return
     */
    @Override
    public List<OperationlogDTO> findAllLogInfo(Page<OperationlogVo> page, OperationLogCommand operationLogCommand) {
        OperationlogDTO operationlogDTO = BeanConverter.convert(operationLogCommand, OperationlogDTO.class);
        return operationlogDao.getAllOperationLogInfo(page,operationlogDTO);

    }
}