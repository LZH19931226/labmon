package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.infrastructure.dao.SysRoleDao;
import com.hc.po.SysRoleEntity;
import com.hc.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleRepositoryImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleRepository {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Override
    public List<SysRoleEntity> getSysRoleList() {
        return sysRoleDao.getSysRoleList();
    }
}
