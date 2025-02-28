package com.hc.clickhouse.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class EquipmentDataParam {
    /**
     * 设备id
     */
    private String equipmentNo;

    /**
     * 医院id
     */
    private String hospitalCode;

    /**
     * 查询字段
     */
    private String field;

    /**
     * 查询的字段集
     */
    private List<String> fieldList;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


    /**
     * 开始时间
     */
    private LocalDateTime localStartTime;

    /**
     * 结束时间
     */
    private LocalDateTime localEndTime;

    /**
     * 筛选条件
     */
    private List<Filter> filterList;

    /** 年月(用于查询数据库) */
    private String yearMonth;

    private String minTime;

    private String maxTime;


    /** 服务器时区 */
    private String clientTimeZone;

    @Data
    public static class Filter{
        /** 筛选字段 */
        private String field;

        /** 条件 */
        private String condition;

        /** 值 */
        private String value;
    }
}
