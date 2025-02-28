package com.hc.clickhouse.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class CurveParam {

    /** 设备id */
    private String equipmentNo;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /**
     * 开始时间
     */
    private LocalDateTime localStartTime;

    /**
     * 结束时间
     */
    private LocalDateTime localEndTime;


    /** 年月 */
    private String yearMonth;


    /** 字段表 */
    private List<String> instrumentConfigIdList;

    /** 服务器时区 */
    private String clientTimeZone;
}
