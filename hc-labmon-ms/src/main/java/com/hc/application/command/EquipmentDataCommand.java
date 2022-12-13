package com.hc.application.command;

import lombok.Data;

import java.util.List;

@Data
public class EquipmentDataCommand {

    /**
     * 设备id
     */
    private String equipmentNo;

    /**
     * 查询字段
     */
    private String field;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 筛选条件
     */
    private List<Filter> filterList;

    /** 年月(用于查询数据库) */
    private String yearMonth;

    /** 当前分页 */
    private Integer pageCurrent;

    /** 分页大小 */
    private Integer pageSize;

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
