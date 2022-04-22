package com.hc.application.command;

import com.hc.dto.MonitorequipmentwarningtimeDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorEquipmentCommand {

    /**
     * 医院编码
     */
    private String hospitalCode;

    /**
     * 设备类型编码
     */
    private String equipmentTypeId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 分页大小
     */
    private Long pageSize;

    /**
     * 当前页
     */
    private Long pageCurrent;

    /**
     * 是否可用
     */
    private Long clientVisible;


    /**
     * 设备类型以及探头信息
     */
    private MonitorinstrumenttypeDTO monitorinstrumenttypeDTO;

    /**
     * sn
     */
    private String sn;

    /**
     * 通道
     */
    private String channel;
    /**
     * 是否启用报警
     */
    private String alwaysAlarm;
    /**
     * 设备型号
     */
    private String equipmentBrand;

    /**
     * 报警时间段
     */
    private List<MonitorequipmentwarningtimeDTO> warningTimeList;

}
