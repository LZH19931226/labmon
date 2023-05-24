package com.hc.appliction;

import com.hc.service.SysRoleService;
import com.hc.vo.user.dto.SysRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysRoleaApplication {
    @Autowired
    private SysRoleService sysRoleService;


    public List<SysRoleDTO> getSysRoleList() {
        return sysRoleService.getSysRoleList();
    }
}
