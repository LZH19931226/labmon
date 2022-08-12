package com.hc.application.response;

import com.hc.dto.InstrumentParamConfigDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WarningRecordInfo {

    /** 设备名称 */
    private String equipmentName;

    /** sn */
    private String sn;

    /** 报警上传时间 */
    private Date inputDateTime;

    /** 设备id */
    private String equipmentNo;

    /** 报警的数值 */
    private String warningValue;

    /** 报警规则 */
    private String alarmRules;

    /** 是否全天报警 */
    private String alwayalarm;

    /** 设备类型id */
    private String equipmentTypeId;

    /** 设备英文名称*/
    private String eName;

    /** 检测下限值 */
    private String lowLimit;

    /** 检测上限值 */
    private String highLimit;

    /** 设备状态 */
    private Long state;

    /** 探头信息 */
    private InstrumentParamConfigDto instrumentParamConfigDto;
}
