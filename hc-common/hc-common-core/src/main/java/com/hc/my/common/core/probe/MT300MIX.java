package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.AirflowEnum;
import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;


/**
 * 设备MT300MIX
 * @author hc
 */
@Data
public class MT300MIX extends PublicMethod{

    /**二氧化碳*/
    private String Co2;

    /**氧气*/
    private String o2;

    /**温度*/
    private String temperature;

    /**气流*/
    private String airflow;

    /**ups*/
    public String ups;

    /**电压*/
    private String voltage;

    /**
     *获取二氧化碳的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCo2(String exceptionCode){
        return getExceptionInfo2(exceptionCode);
    }

    /**
     * 获取氧气的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierO2(String exceptionCode){
        return getExceptionInfo2(exceptionCode);
    }

    /**
     * 获取温度的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.F000.name())){
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        return null;
    }

    /**
     * 获取气流的信息
     * @param info
     * @return
     */
    public String getAirflowInfo(String info){
        if(StringUtils.equals(info, AirflowEnum.ONE.name())){
            return AirflowEnum.NORMAL_TO_EARLY_WARNING.name();
        }
        if(StringUtils.equals(info, AirflowEnum.TWO.name())){
            return AirflowEnum.ALERT_TO_NORMAL.name();
        }
        if(StringUtils.equals(info, AirflowEnum.THREE.name())){
            return AirflowEnum.ALERT_TO_ALARM.name();
        }
        if(StringUtils.equals(info, AirflowEnum.FOUR.name())){
            return AirflowEnum.ALARM_TO_OFF.name();
        }
        if(StringUtils.equals(info, AirflowEnum.FIVE.name())){
            return AirflowEnum.NORMAL_OPERATION_OF_EQUIPMENT.name();
        }
        if(StringUtils.equals(info, AirflowEnum.SIX.name())){
            return AirflowEnum.EQUIPMENT_LEAKAGE_WARNING.name();
        }
        if(StringUtils.equals(info, AirflowEnum.SEVEN.name())){
            return AirflowEnum.EQUIPMENT_LEAKAGE_ALARM.name();
        }
        if(StringUtils.equals(info, AirflowEnum.EIGHT.name())){
            return AirflowEnum.CLOSE_THE_AIR_LEAKAGE_ALARM.name();
        }
        if(StringUtils.equals(info, AirflowEnum.NINE.name())){
            return AirflowEnum.LOW_AIR_PRESSURE_ALARM_OF_EQUIPMENT.name();
        }
        if(StringUtils.equals(info, AirflowEnum.TEN.name())){
            return AirflowEnum.MANUALLY_CLOSE_THE_LOW_AIR_PRESSURE_ALARM.name();
        }
        if(StringUtils.equals(info, AirflowEnum.ELEVEN.name())){
            return AirflowEnum.SLEEP_MODE.name();
        }
        return null;
    }

    /**
     * 获取ups信息
     * @param exceptionCode
     * @return
     */
    public String getEnumUps(String exceptionCode){
        return getUpsInfo(exceptionCode);
    }

    /**
     * 获取电压值
     * @param exceptionCode
     * @return
     */
    public String getVoltageInfo(String exceptionCode){
        return null;
    }
}
