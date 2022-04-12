package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.UserBackPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hc
 */
@Mapper
public interface UserBackDao extends BaseMapper<UserBackPo> {

}
