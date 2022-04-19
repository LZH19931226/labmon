package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.dto.UserRightDto;
import com.hc.repository.UserRightRepository;
import com.hc.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户权限服务实现类
 * @author hc
 */
@Service
public class UserRightServiceImpl implements UserRightService {

    @Autowired
    private UserRightRepository userRightRepository;

    @Override
    public List<UserRightDto> findUserRightList(Page page, UserRightCommand userRightCommand) {
        return    userRightRepository.findUserRightList(page,userRightCommand);
    }

    @Override
    public void insertUserRightInfo(UserRightCommand userRightCommand) {
        String username = userRightCommand.getUsername();
    }
}
