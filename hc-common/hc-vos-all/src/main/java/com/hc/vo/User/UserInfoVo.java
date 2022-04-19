package com.hc.vo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author user
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {

    /**用户ID*/
    private String userid;

    /**用户名称*/
    private String username;

    /**用户密码*/
    private String pwd;

}
