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
     * 设备名称
     */
    private String equipmentName;

    /**
     * 查询字段
     */
    private String field;

    /**
     * 查询的字段集
     */
    private List<String> fieldList;

    /**
     *  医院id
     *
     */
    private String hospitalCode;

    /**
     * 设备是否启用（0=未启用 1=启用 空为全部）
     */
    private String clientVisible;

    /**
     * 开始时间
     * 格式：YYYY-MM-dd HH:mm:ss
     */
    private String startTime;

    /**
     * 结束时间
     * 格式：YYYY-MM-dd HH:mm:ss
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

    private List<String> timeList;

    /**
     *格式：HH:mm
     */
    private String maxTime;

    /**
     * 格式：HH:mm
     */
    private String minTime;


    /**
     * 中英文
     */
    private String lang;

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
