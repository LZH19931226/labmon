package com.hc.vo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户权限试图对象
 * @author hc
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRightVo {
    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 密码 */
    private String pwd;

    /** 医院名称 */
    private String hospitalName;

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

    /**
     * 超时联系人
     */
    private String timeout;

    /**
     * 超时警告
     */
    private String timeoutWarning;
}
