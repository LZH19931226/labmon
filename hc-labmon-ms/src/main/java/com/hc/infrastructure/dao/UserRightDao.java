package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.UserRightDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRightDao extends BaseMapper<UserRightDto> {
}
