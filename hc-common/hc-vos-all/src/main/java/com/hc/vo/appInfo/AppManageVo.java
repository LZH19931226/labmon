package com.hc.vo.appInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Builder
@Getter
public class AppManageVo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 客户端设备id 1安卓pad,2 ios
     */
    @ApiModelProperty(value = "客户端设备id 1安卓pad,2 ios")
    private Integer appId;

    /**
     * 大版本号id
     */
    @ApiModelProperty(value = "大版本号id")
    private Integer versionId;

    /**
     * 小版本号
     */
    @ApiModelProperty(value = "小版本号")
    private Integer versionMini;

    /**
     * 版本标识 1.2
     */
    @ApiModelProperty(value = "版本标识 1.2")
    private String  versionCode;

    /**
     * 是否升级 1升级，0不升级，2强制升级
     */
    @ApiModelProperty(value = "是否升级 1升级，0不升级，2强制升级")
    private Integer type;

    /**
     * apk下载url
     */
    @ApiModelProperty(value = "apk下载url")
    private String apkUrl;

    /**
     * 升级提示
     */
    @ApiModelProperty(value = "升级提示")
    private String upgradePoint;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
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
     * 上传的文件
     */
    private MultipartFile multipartFile;
}
