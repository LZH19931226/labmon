package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "version_upgrade")
public class AppVersionManagePo {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 客户端设备id 1安卓pad,2 ios
     */
    @TableField(value = "app_id")
    private Integer appId;

    /**
     * 大版本号id
     */
    @TableField(value = "version_id")
    private Integer versionId;

    /**
     * app名称
     */
    @TableField(value = "app_Name")
    private String appName;

    /**
     * 小版本号
     */
    @TableField(value = "version_mini")
    private Integer versionMini;

    /**
     * 版本标识 1.2
     */
    @TableField(value = "version_code")
    private String  versionCode;

    /**
     * 是否升级 1升级，0不升级，2强制升级
     */
    private Integer type;

    /**
     * apk下载url
     */
    @TableField(value = "apk_url")
    private String apkUrl;

    /**
     * 升级提示
     */
    @TableField(value = "upgrade_point")
    private String upgradePoint;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}
