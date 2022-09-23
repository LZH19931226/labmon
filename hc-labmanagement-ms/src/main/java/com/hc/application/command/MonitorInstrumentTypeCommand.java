package com.hc.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MonitorInstrumentTypeCommand {

    /**
     * 设备id
     */
    private Integer instrumentTypeId;

    /**
     * 设备name
     */
    private String instrumentTypeName;

    /**
     * 设备类型id
     */
    private List<String> equipmentTypeId;

    /**
     * 当前分页
     */
    private Integer  pageCurrent;

    /**
     * 分页大小
     */
    private Integer pageSize;

}
