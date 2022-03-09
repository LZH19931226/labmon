package com.hc.model.RequestModel;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("添加设备类型信息请求参数模型")
public class EquipmentTypeInfoModel {
    @NotNull
    private String equipmenttypeid;
    @NotNull
    private String hospitalcode;

    private String isvisible;

    private String timeout;

    private Integer timeouttime;
    private String usernames;
    private List<EquipmentInfoModel> equipmentInfoModelList;

    /**
     * 全天报警
     */
    private String alwayalarm ;

    /**
     * 工作报警时间段
     */
    private WorkTimeBlockModel[] workTimeBlock;

    /**
     * 移除工作报警时间段
     */
    private WorkTimeBlockModel[] deleteWarningTimeBlock;
}
