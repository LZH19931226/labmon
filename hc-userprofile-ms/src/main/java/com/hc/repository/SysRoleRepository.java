package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.po.SysRoleEntity;

import java.util.List;

public interface SysRoleRepository  extends IService<SysRoleEntity> {


    List<SysRoleEntity> getSysRoleList();
}
