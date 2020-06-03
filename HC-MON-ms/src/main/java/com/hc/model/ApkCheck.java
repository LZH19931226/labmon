package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("apk更新模型")
@Getter
@Setter
@ToString
public class ApkCheck {

    @ApiModelProperty("最新版本号")
    private  String newversion;

    @ApiModelProperty("下载地址")
    private  String  url;

    //0:不用 1:强制
    @ApiModelProperty("是否强制更新")
    private  String  forceUpdate;

    //0：不用 1：用
    @ApiModelProperty("是否更新")
    private  String  Update;

    @ApiModelProperty("最新文件名称")
    private  String  filename;


    @ApiModelProperty("更新说明")
    private  String  updateInfo;

    @ApiModelProperty("版本名称")
    private  String  versionname;

    @ApiModelProperty("文件大小")
    private  String  size;
}
