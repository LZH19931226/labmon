package com.hc.service;

import com.hc.vo.user.dto.SysMenuDTO;

import java.util.List;


public interface SysMenuEntityService {

    /**
     * 查询用户菜单列表
     *
     * @param roleName 角色名称
     */
    List<SysMenuDTO> getUserMenuList(String roleName);
}
