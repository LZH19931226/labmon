package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.po.SysMenuEntity;

import java.util.List;

public interface SysMenuEntityRepository extends IService<SysMenuEntity> {

    /**
     * 查询用户菜单列表
     *
     * @param roleName 角色名称
     */
    List<SysMenuEntity> getUserMenuList(String roleName);
}
