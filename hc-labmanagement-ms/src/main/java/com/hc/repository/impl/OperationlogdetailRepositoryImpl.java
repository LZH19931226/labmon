package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.dto.OperationlogdetailDTO;
import com.hc.infrastructure.dao.OperationlogdetailDao;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.OperationlogdetailPo;
import com.hc.repository.OperationlogdetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class OperationlogdetailRepositoryImpl extends ServiceImpl<OperationlogdetailDao,OperationlogdetailPo> implements OperationlogdetailRepository  {

    @Autowired
    private  OperationlogdetailDao operationlogdetailDao;

    /**
     * 获取日志详细信息
     *
     * @param logId
     * @return
     */
    @Override
    public List<OperationlogdetailDTO> getDetailedLogById(String logId) {
        List<OperationlogdetailPo> operationlogdetailPos = operationlogdetailDao.selectList(Wrappers.lambdaQuery(new OperationlogdetailPo())
                .eq(OperationlogdetailPo::getLogid, logId));
        return BeanConverter.convert(operationlogdetailPos,OperationlogdetailDTO.class);
    }
}