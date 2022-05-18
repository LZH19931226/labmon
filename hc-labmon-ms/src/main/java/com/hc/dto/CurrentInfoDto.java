package com.hc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CurrentInfoDto {
    /** PM2.5 */
    private Object PM25;
    /** PM10 */
    private Object PM10;
    /** CO2 */
    private Object CO2;
    /** 氧气 */
    private Object O2;
    /** 湿度 */
    private Object RH;
    /** 甲醛 */
    private Object JQ;
    /** 压力 */
    private Object PRESS;
    /** 空气质量 */
    private Object VOC;
    /** 温度 */
    private Object TEMP;
    /** 门 */
    private Object DOOR;
    /** 电量 */
    private Object QC;
}
