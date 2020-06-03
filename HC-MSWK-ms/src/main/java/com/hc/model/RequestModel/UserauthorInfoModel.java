package com.hc.model.RequestModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("用户权限修改请求参数")
public class UserauthorInfoModel {

    private String userid;

    private String equipmenttypeid;

    private String type;    // 0: 删除权限   1：添加权限
}
