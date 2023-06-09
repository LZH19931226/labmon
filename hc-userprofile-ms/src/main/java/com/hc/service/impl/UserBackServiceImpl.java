package com.hc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserCommand;
import com.hc.dto.UserBackDto;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserBackPo;
import com.hc.repository.UserBackRepository;
import com.hc.service.UserBackService;
import com.hc.vo.user.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author hc
 */
@Service
public class UserBackServiceImpl  implements UserBackService {

    @Autowired
    private UserBackRepository userBackRepository;

    /**
     * 用户登录
     * @param userCommand 用户数据传输对象
     * @return 用户对象
     */
    @Override
    public UserBackDto userLogin(UserCommand userCommand) {
        String username = userCommand.getUsername();
        String lang = userCommand.getLang();
        if(StringUtils.isBlank(lang)){
            throw new IedsException(LabSystemEnum.LANG_NOT_NULL.name());
        }
        if(StringUtils.isBlank(username)){
            if(lang.equals("zh")){
                throw new IedsException(LabSystemEnum.USERNAME_NOT_NULL.getMessage());
            }else {
                throw new IedsException(LabSystemEnum.USERNAME_NOT_NULL.name());
            }
        }
        String pwd = userCommand.getPwd();
        if(StringUtils.isBlank(pwd)){
            if(lang.equals("zh")){
                throw new IedsException(LabSystemEnum.PASSWORD_CAN_NOT_NULL.getMessage());
            }else {
                throw new IedsException(LabSystemEnum.PASSWORD_CAN_NOT_NULL.name());
            }
        }
        UserBackPo userBackPo =BeanConverter.convert(userCommand, UserBackPo.class);
        return userBackRepository.userLogin(userBackPo,lang);
    }

    /**
     * 修改用户信息
     * @param userCommand 用户参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserCommand userCommand) {
        String userid = userCommand.getUserid();
        if(StringUtils.isBlank(userid)){
            throw new IedsException(LabSystemEnum.USERID_NOT_NULL);
        }
        String username = userCommand.getUsername();
        Integer integer = userBackRepository.selectOne(username);
        if(integer>1){
            throw new IedsException(LabSystemEnum.USERNAME_NOT_NULL);
        }
        UserBackPo userBackPo = BeanConverter.convert(userCommand,UserBackPo.class);
        userBackRepository.updateUserInfo(userBackPo);
    }

    /**
     * 根据用户id查询user信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public UserBackDto selectUserBackByUserId(String userId) {
        return userBackRepository.selectUserBackByUserId(userId);
    }

    /**
     * 分页获取后台人员信息
     *
     * @param page 分页对象
     * @param userCommand 用户参数对象
     * @return 用户集合
     */
    @Override
    public List<UserBackDto> findUserAllInfo(Page<UserInfoVo> page, UserCommand userCommand) {
        return userBackRepository.findUserAllInfo(page,userCommand);
    }

    /**
     * 删除用户信息
     *
     * @param userid 用户id
     */
    @Override
    public void deleteUserInfo(String[] userid) {
        userBackRepository.deleteUserInfo(userid);
    }

    /**
     * 新增用户信息
     *
     * @param userCommand 用户参数对象
     */
    @Override
    public void insertUserInfo(UserCommand userCommand) {
        String username = userCommand.getUsername();
        UserBackDto userBackDto = userBackRepository.selectUserBackByUsername(username);
        if(!ObjectUtils.isEmpty(userBackDto)){
            throw new IedsException(LabSystemEnum.USERNAME_NOT_NULL);
        }
        userBackRepository.insertUserInfo(userCommand);
    }
}
