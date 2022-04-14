package com.hc.dto;

import com.hc.po.UserBackPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserBackDto extends UserBackPo {

    /**用户ID*/
    private String userid;

    /**用户名称*/
    private String username;

    /**用户密码*/
    private String pwd;

}
