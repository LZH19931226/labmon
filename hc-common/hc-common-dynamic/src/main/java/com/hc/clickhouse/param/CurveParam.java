package com.hc.clickhouse.param;

import lombok.Data;

import java.util.List;

@Data
public class CurveParam {

    /** 设备id */
    private String equipmentNo;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /** 年月 */
    private String yearMonth;

    /** 字段表 */
    private List<String> instrumentConfigIdList;
}
