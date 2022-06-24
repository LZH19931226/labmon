package com.hc.my.common.core.bean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author LiuZhiHao
 * @date 2021/5/28 10:52
 * 描述: 各服务注册中心实例名称
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationName {

    /**
     * 用户服务
     */
    public static final String USER = "HC-USERPROFILE-ms";


    /**
     * 网关服务
     */
    public static final String OPENWAY = "HC-OPENWAY-ms";


    /**
     * 后台管理
     */
    public static final String LABMANAGENMENT = "HC-LABMANAGEMENT-ms";


    /**
     * TCP
     */
    public static final String TCP = "HC-TCP-ms";


    /**
     * redis
     */
    public static final String REDIS = "HC-LABREDIS-ms";


    /**
     * msct
     */
    public static final String MSCT = "HC-MSCT-ms";

}
