package com.hc.vo.user;

import com.hc.command.labmanagement.model.HospitalEquipmentTypeModel;
import com.hc.vo.hospital.HospitalInfoVo;
import com.hc.vo.user.dto.SysMenuDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户权限试图对象
 * @author hc
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRightVo {

    /** 用户id */
    private String userid;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 密码 */
    private String pwd;

    /** 医院名称 */
    private String hospitalName;

    /** 医院编码 */
    private String hospitalCode;

    /** 手机号 */
    private String phoneNum;

    /** 是否启用 */
    private Long isUse;

    /** 用户角色 */
    private String userType;

    /**
     * 推送设备类型
     */
    private String deviceType;

    /** 报警方式 */
    private String reminders;
    /**
     * 超时联系人
     */
    private String timeout;

    /**
     * 超时警告
     */
    private String timeoutWarning;

    /**
     * 双验证登录 为空时不需要 为1时需要
     */
    private String twoFactorLogin;

    /**
     * 用户权限
     */
    private String role;

    private List<HospitalEquipmentTypeModel> hospitalEquipmentTypeModels;

    /** 医院信息 */
    private HospitalInfoVo hospitalInfoVo;

    private String token;

    /** 语种 zh表示中文 en表示英文 */
    private String lang;

    /**角色关联的菜单 */
    private List<SysMenuDTO> sysMenuDTOS;

    /** 邮箱 */
    private String mailbox;
}
