package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.SysMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuEntityDao extends BaseMapper<SysMenuEntity> {


    /**
     * 查询用户菜单列表
     *
     * @param roleName 角色名称
     */
    List<SysMenuEntity> getUserMenuList(@Param("roleName") String roleName);


}
