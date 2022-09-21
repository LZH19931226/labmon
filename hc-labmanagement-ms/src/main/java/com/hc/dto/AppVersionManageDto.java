package com.hc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AppVersionManageDto {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 客户端设备id 1安卓pad,2 ios
     */
    private Integer appId;

    /**
     * 大版本号id
     */
    private Integer versionId;

    /**
     * app名称
     */
    private String appName;

    /**
     * 小版本号
     */
    private Integer versionMini;

    /**
     * 版本标识 1.2
     */
    private String  versionCode;

    /**
     * 是否升级 1升级，0不升级，2强制升级
     */
    private Integer type;

    /**
     * apk下载url
     */
    private String apkUrl;

    /**
     * 升级提示
     */
    private String upgradePoint;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 当前分页
     */
    private Integer pageCurrent;

    /**
     * 分页大小
     */
    private Integer pageSize;
}
