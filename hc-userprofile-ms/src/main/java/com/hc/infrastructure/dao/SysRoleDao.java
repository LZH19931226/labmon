package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.SysRoleEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleDao  extends BaseMapper<SysRoleEntity> {

    @Select("select * from  sys_role")
    List<SysRoleEntity> getSysRoleList();
}
