package com.hc.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-06.
 */
@ApiModel("用户信息模型")
@Getter
@Setter
@ToString
public class ClientInfoModel {

    private String hospitalname;

    private String userid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 是否可用
     */
    private Boolean isuse;

    /**
     * 电话号码
     */
    private String phonenum;

    private String devicetype;

    private String devicetoken;


}
