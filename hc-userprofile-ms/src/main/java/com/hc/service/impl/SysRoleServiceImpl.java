package com.hc.service.impl;

import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.SysRoleEntity;
import com.hc.repository.SysRoleRepository;
import com.hc.service.SysRoleService;
import com.hc.vo.user.dto.SysRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Override
    public List<SysRoleDTO> getSysRoleList() {
        List<SysRoleEntity> list = sysRoleRepository.getSysRoleList();
        return BeanConverter.convert(list,SysRoleDTO.class);
    }
}
