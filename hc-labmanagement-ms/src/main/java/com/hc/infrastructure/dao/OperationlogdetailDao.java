package com.hc.infrastructure.dao;

import com.hc.po.OperationlogdetailPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统操作日志详细表
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Mapper
public interface OperationlogdetailDao extends BaseMapper<OperationlogdetailPo> {
	
}
