package com.hc.service.impl;

import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.TreeUtils;
import com.hc.po.SysMenuEntity;
import com.hc.repository.SysMenuEntityRepository;
import com.hc.service.SysMenuEntityService;
import com.hc.vo.user.dto.SysMenuDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuEntityServiceImpl  implements SysMenuEntityService {

    @Autowired
    private SysMenuEntityRepository sysMenuEntityRepository;

    @Override
    public List<SysMenuDTO> getUserMenuList(String roleName) {
        List<SysMenuEntity> menuList = sysMenuEntityRepository.getUserMenuList(roleName);
        if (CollectionUtils.isEmpty(menuList)){
            return null;
        }
        List<SysMenuDTO> dtoList = BeanConverter.to(menuList, SysMenuDTO.class);
        return TreeUtils.build(dtoList);
    }
}
