package com.hc.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.infrastructure.dao.SysMenuEntityDao;
import com.hc.po.SysMenuEntity;
import com.hc.repository.SysMenuEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysMenuEntityRepositoryImpl extends ServiceImpl<SysMenuEntityDao,SysMenuEntity> implements SysMenuEntityRepository {


    @Autowired
    private SysMenuEntityDao sysMenuEntityDao;

    @Override
    public List<SysMenuEntity> getUserMenuList(String roleName) {
        return sysMenuEntityDao.getUserMenuList(roleName);
    }
}
