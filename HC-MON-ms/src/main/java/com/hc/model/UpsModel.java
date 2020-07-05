package com.hc.model;

import lombok.Data;

/**
 * @author LiuZhiHao
 * @date 2020/7/5 10:24
 * 描述:
 **/
@Data
public class UpsModel {

    private String equipmentno;

    private String equipmentname;

    /**
     * 当前市电是否异常
     */
    private String currentups;

    /**
     * 电压
     */
    private String voltage;
}
