package com.hc.model;

import java.util.List;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorupsrecord;
import com.hc.utils.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-07-31.
 */
@Getter
@Setter
@ToString
@ApiModel("用户登录返回参数模型")
public class LoginResponseModel {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户id")
    private String userid;

    /**
     * 医院编号
     */
    @ApiModelProperty("医院编号id")
    private String hospitalcode;

    @ApiModelProperty("医院名称")
    private String hospitalname;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String pwd;

    /**
     * 是否可用
     */
    private Boolean isuse;

    /**
     * 电话号码
     */
    @ApiModelProperty("电话号码")
    private String phonenum;

    /**
     * DeviceToken
     */
    private String devicetoken;

    private String usertype;

    /**
     * 推送设备类型
     */
    private String devicetype;
    @ApiModelProperty("监控医院设备类型")
    private List<MonitorequipmenttypeModel> hospitalEquipmentType;

    @ApiModelProperty("登录验证token")
    private String token;

    @ApiModelProperty("分页展示当前设备当前值")
    private ApiResponse<Page<Monitorequipment>> monitorequipments;

    @ApiModelProperty("市电当前值")
    private ApiResponse<Monitorupsrecord> monitorupsrecord;

}
