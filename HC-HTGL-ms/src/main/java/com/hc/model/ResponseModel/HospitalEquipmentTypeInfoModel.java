package com.hc.model.ResponseModel;

import com.alibaba.fastjson.annotation.JSONField;
import com.hc.entity.MonitorEquipmentWarningTime;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("展示所有医院设备类型信息")
@Component
public class HospitalEquipmentTypeInfoModel {
    private String hospitalcode;

    private String equipmenttypeid;

    private String hospitalname;

    private String equipmenttypename;

    private String isvisible;

    private String  timeout;

    private Integer timeouttime;

    /**
     * 全天报警
     *  1=全天报警
     *  0=不全天报警
     */
    private String alwayalarm ;

    /**
     * 报警时间段
     */
    List<MonitorEquipmentWarningTime> warningTimeList;
}
