package com.hc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class MonitorUpsInfoDto  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * pkid
     */
    @Id
    private String pkId;

    /**
     * 探头编号
     */
    private String equipmentNo;

    /**
     * 电源状态
     */
    private String ups;

    /**
     * 记录时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date inputDatetime;
}
