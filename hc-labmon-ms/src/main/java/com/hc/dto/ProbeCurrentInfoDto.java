package com.hc.dto;

import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProbeCurrentInfoDto {

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * sn号
     */
    private String sn;

    /**
     * 最新的上传时间
     */
    private Date inputTime;

    /**
     * 设备id
     */
    private String equipmentNo;

    /**
     * 设备型号id
     */
    private String instrumentTypeId;

    /**
     * 设备类型id
     */
    private String equipmentTypeId;

    /**
     * 设备状态
     */
    private String state;

    /** 公司 */
    private String company;

    /** 品牌*/
    private String brand;

    /** 型号 */
    private String model;


    /**
     * 探头集合
     */
    private List<ProbeInfoDto> probeInfoDtoList;

    /**
     * 检测类型id集合
     */
    private List<String> instrumentConfigIdList;
}
