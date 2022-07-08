package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserRightCommand;
import com.hc.command.labmanagement.user.UserRightInfoCommand;
import com.hc.command.labmanagement.user.UserRightLogCommand;
import com.hc.constant.UserRightEnumCode;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.dto.UserBackDto;
import com.hc.dto.UserRightDto;
import com.hc.labmanagent.OperationlogApi;
import com.hc.msct.sms.SmsApi;
import com.hc.my.common.core.constant.enums.*;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.phone.PhoneCodeApi;
import com.hc.service.HospitalRegistrationInfoService;
import com.hc.service.UserBackService;
import com.hc.service.UserRightService;
import com.hc.vo.user.UserRightVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户权限申请
 * @author hc
 */
@Component
public class UserRightApplication {

    @Autowired
    private UserRightService userRightService;

    @Autowired
    private OperationlogApi operationlogApi;

    @Autowired
    private  UserBackService userBackService;

    @Autowired
    private HospitalRegistrationInfoService hospitalRegistrationInfoService;

    @Autowired
    private PhoneCodeApi phoneCodeApi;

    @Autowired
    private SmsApi smsApi;

    /**
     * 根据分页信息查询用户权限信息
     * @param userRightCommand  用户权限命令
     * @param pageSize 分页大小
     * @param pageCurrent 当前分页
     * @return
     */
    public Page<UserRightVo> findUserRightList(UserRightCommand userRightCommand, Long pageSize, Long pageCurrent) {
        Page<UserRightVo> page = new Page<>(pageCurrent,pageSize);
        List<UserRightDto> userRightList = userRightService.findUserRightList(page, userRightCommand);
        List<UserRightVo> list = new ArrayList<>();
        if(userRightList!=null && userRightList.size()!=0){
            userRightList.forEach(res ->{
                UserRightVo result = UserRightVo.builder()
                        .hospitalCode(res.getHospitalCode())
                        .userid(res.getUserid())
                        .username(res.getUsername())
                        .nickname(StringUtils.isEmpty(res.getNickname())?res.getUsername():res.getNickname())
                        .pwd(res.getPwd())
                        .hospitalName(res.getHospitalName())
                        .phoneNum(res.getPhoneNum())
                        .isUse(res.getIsUse())
                        .userType(res.getUserType())
                        .deviceType(res.getDeviceType())
                        .timeout(res.getTimeout()==null?"":res.getTimeout())
                        .timeoutWarning(res.getTimeoutWarning()==null?"":res.getTimeoutWarning())
                        .reminders(res.getReminders()==null?"":res.getReminders())
                        .role(res.getRole() == null?"":res.getRole())
                        .build();
                list.add(result);
            });
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 新增用户信息
     * @param userRightCommand 用户参数对象
     */
    public void insertUserRightInfo(UserRightCommand userRightCommand) {
        userRightService.insertUserRightInfo(userRightCommand);
        UserRightInfoCommand userRightInfoCommand = build(Context.getUserId(),
                 new UserRightLogCommand(),
                 userRightCommand,
                 OperationLogEunm.USER_INFO.getCode(),
                 OperationLogEunmDerailEnum.ADD.getCode());
        operationlogApi.addUserRightLog(userRightInfoCommand);
    }

    /**
     * 构建日志对象
     * @param userId 用户id
     * @param oldInfo 旧的用户对象
     * @param newInfo 新的用户对象
     * @param type 模块类型
     * @param operationType 操作类型
     * @return 用户日志信息对象
     */
    private UserRightInfoCommand build(String userId, UserRightLogCommand oldInfo, UserRightCommand newInfo, String type, String operationType) {
        UserRightInfoCommand userRightInfoCommand = new UserRightInfoCommand();
        //查询用户信息
        UserBackDto userBackDto = userBackService.selectUserBackByUserId(userId);
        if(!ObjectUtils.isEmpty(userBackDto)){
            userRightInfoCommand.setUsername(userBackDto.getUsername());
        }
        //查询医院信息
        String hospitalCode = oldInfo.getHospitalCode()==null?newInfo.getHospitalCode():oldInfo.getHospitalCode();
        HospitalRegistrationInfoDto hospitalInfo = hospitalRegistrationInfoService.findHospitalInfoByCode(hospitalCode);
        if(!ObjectUtils.isEmpty(hospitalInfo)){
            userRightInfoCommand.setHospitalName(hospitalInfo.getHospitalName());
        }
        userRightInfoCommand.setType(type);
        userRightInfoCommand.setOperationType(operationType);
        UserRightLogCommand convert = BeanConverter.convert(newInfo, UserRightLogCommand.class);
        userRightInfoCommand.setNewUserRight(convert);
        userRightInfoCommand.setOldUserRight(oldInfo);
        return userRightInfoCommand;
    }

    /**
     * 修改用户信息
     * @param userRightCommand 用户参数
     */
    public void updateUserRightInfo(UserRightCommand userRightCommand) {
        UserRightDto userRightDto = userRightService.selectUserRightInfo(userRightCommand.getUserid());
        userRightService.updateUserRightInfo(userRightCommand);
        UserRightLogCommand convert = BeanConverter.convert(userRightDto, UserRightLogCommand.class);
        UserRightInfoCommand build = build(Context.getUserId(),
                convert,
                userRightCommand,
                OperationLogEunm.USER_INFO.getCode(),
                OperationLogEunmDerailEnum.EDIT.getCode());
        operationlogApi.addUserRightLog(build);
    }

    /**
     * 删除用户信息
     * @param userRightCommand 用户参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserRightInfo(UserRightCommand userRightCommand) {
        UserRightDto userRightDto = userRightService.selectUserRightInfo(userRightCommand.getUserid());
        userRightService.deleteUserRightInfo(userRightCommand);
        UserRightLogCommand command = BeanConverter.convert(userRightDto, UserRightLogCommand.class);
        UserRightInfoCommand build =
                build(Context.getUserId(), command, new UserRightCommand(), OperationLogEunm.USER_INFO.getCode(), OperationLogEunmDerailEnum.REMOVE.getCode());
        operationlogApi.addUserRightLog(build);
    }

    /**
     * 账号密码登录
     * @param userRightCommand 用户参数
     * @return 用户视图对象
     */
    public UserRightVo Login(UserRightCommand userRightCommand) {
        //1.判断账号密码是否正确
        //2.手机号是否注册
        //3.判断登录端是否安卓 3.1不是安卓 返回用户信息 3.2是安卓 go  3.2.1未设置双因子 go 3.2.1设置双因子 是否一次登录 是校验 不是 go
        UserRightDto userRightDto = userRightService.selectUserRight(userRightCommand);
        String phoneNum = userRightDto.getPhoneNum();
        if (StringUtils.isEmpty(phoneNum)) {
            throw new IedsException(UserRightEnumCode.THE_ACCOUNT_IS_THE_REGISTERED_MOBILE_PHONE_NUMBER.getMessage());
        }
        String loginType = userRightCommand.getLoginType();
        String loginStatus = userRightCommand.getLoginStatus();
        String hospitalCode = userRightDto.getHospitalCode();
        //查询医院信息
        HospitalRegistrationInfoDto hospitalInfo = hospitalRegistrationInfoService.findHospitalInfoByCode(hospitalCode);
        //未设置双因子的医院直接放行 设置了的医院在非app上登录直接放行
        if (!HospitalInfoEnum.ONE.getCode().equals(hospitalInfo.getFactor()) || LoginTypeEnum.H5.getCode().equals(loginType)) {
            return builder(userRightDto,hospitalInfo.getHospitalName());
        }
        //是在app上登录并且是非第一次登录 LoginStatus：0为第一次登录
        if(LoginTypeEnum.ANDROID.getCode().equals(loginType) && LoginStatusEnum.ONE.getCode().equals(loginStatus)){
            return builder(userRightDto,hospitalInfo.getHospitalName());
        }
        return UserRightVo.builder()
                .twoFactorLogin(TwoFactorLoginEnum.ONE.getCode())
                .username(userRightDto.getUsername())
                .pwd(userRightDto.getPwd())
                .isUse(userRightDto.getIsUse())
                .hospitalCode(userRightDto.getHospitalCode())
                .userid(userRightDto.getUserid())
                .phoneNum(userRightDto.getPhoneNum())
                .userType(userRightDto.getUserType())
                .hospitalName(hospitalInfo.getHospitalName()).build();
    }

    /**
     * 构建用户信息
     * @param userRightDto
     * @return
     */
    private UserRightVo builder(UserRightDto userRightDto,String hospitalName) {
        return UserRightVo.builder()
                .username(userRightDto.getUsername())
                .pwd(userRightDto.getPwd())
                .isUse(userRightDto.getIsUse())
                .hospitalCode(userRightDto.getHospitalCode())
                .userid(userRightDto.getUserid())
                .phoneNum(userRightDto.getPhoneNum())
                .userType(userRightDto.getUserType())
                .hospitalName(hospitalName).build();
    }

    /**
     * 查询该医院的用户信息
     * @param hospitalCode 医院id
     * @return 用户缓存集合
     */
    public List<UserRightRedisDto> findALLUserRightInfo(String hospitalCode) {
        List<UserRightDto> userRightDtoList =  userRightService.findALLUserRightInfoByHospitalCode(hospitalCode);
        if(CollectionUtils.isEmpty(userRightDtoList)){
            return null;
        }
        return BeanConverter.convert(userRightDtoList,UserRightRedisDto.class);
    }

    /**
     * 生成验证码
     * @param phoneNum
     * @return
     */
    public void getPhoneCode(String phoneNum) {
        String code = builderCode();
        code = "111111";
        phoneCodeApi.addPhoneCode(phoneNum,code);
        smsApi.senMessagecode(phoneNum,code);
    }

    public String builderCode(){
        return (Math.random() + "").substring(2, 2 + 6);
    }
    /**
     * 手机号登录
     * @param userRightCommand
     * @return
     */
    public UserRightVo userRightLoginByPhone(UserRightCommand userRightCommand) {
        String phoneNum = userRightCommand.getPhoneNum();
        if (StringUtils.isEmpty(phoneNum)) {
            throw new IedsException(UserRightEnumCode.MOBILE_NUMBER_CANNOT_BE_EMPTY.getMessage());
        }
        String code = userRightCommand.getCode();
        if (StringUtils.isEmpty(code)) {
            throw new IedsException(UserRightEnumCode.VERIFICATION_CODE_MUST_BE_FILLED.getMessage());
        }
        String result = phoneCodeApi.getPhoneCode(phoneNum).getResult();
        if (StringUtils.isEmpty(result)) {
            throw new IedsException(UserRightEnumCode.VERIFICATION_CODE_HAS_EXPIRED.getMessage());
        }
        if(!result.equals(code)){
            throw new IedsException(UserRightEnumCode.VERIFICATION_CODE_ERROR.getMessage());
        }
        return UserRightVo.builder()
                .hospitalCode(userRightCommand.getHospitalCode())
                .username(userRightCommand.getUsername())
                .pwd(userRightCommand.getPwd())
                .isUse(userRightCommand.getIsUse())
                .hospitalCode(userRightCommand.getHospitalCode())
                .userid(userRightCommand.getUserid())
                .phoneNum(userRightCommand.getPhoneNum())
                .userType(userRightCommand.getUserType())
                .hospitalName(userRightCommand.getHospitalName())
                .build();
    }
}
