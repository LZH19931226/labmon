package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.dto.OperationlogDTO;
import com.hc.po.OperationlogPo;
import com.hc.vo.backlog.OperationlogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统操作日志表
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */

public interface OperationlogDao extends BaseMapper<OperationlogPo> {

    /**
     * 分页获取日志信息
     * @param page
     * @param operationlogDTO
     * @return
     */
    List<OperationlogDTO> getAllOperationLogInfo(Page<OperationlogVo> page, @Param(value = "operationlogDTO") OperationlogDTO operationlogDTO);

//    List<OperationlogDTO> getAllOperationLogInfo(Page<OperationlogVo> page,
//                                                 @Param(value = "hospitalname") String hospitalname,
//                                                 @Param(value = "opeartiontype") String opeartiontype,
//                                                 @Param(value = "functionname") String functionname,
//                                                 @Param(value = "username") String username,
//                                                 @Param(value = "begintime") Date begintime,
//                                                 @Param(value = "endtime") Date endtime);
}
