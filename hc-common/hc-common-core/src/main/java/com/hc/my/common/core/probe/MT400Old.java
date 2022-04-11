package com.hc.my.common.core.probe;


import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT400旧
 * @author hc
 */
@Data
public class MT400Old extends PublicMethod{

    /**二氧化碳*/
    private String co2;

    /**氧气*/
    private String o2;

    /**空气质量*/
    private String voc;

    /**温度*/
    private String temperature;

    /**相对温度*/
    private String rh;

    /**气压*/
    private String press;

    /**pm2.5*/
    private String pm25;

    /**pm10*/
    private String pm10;

    /**甲醛*/
    private String formaldehyde;

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
     * 获取空气质量的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierVoc(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        return null;
    }

    /**
     * 获取温度的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature(String exceptionCode){
        return null;
    }

    /**
     * 获取相对温度的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRh(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        return null;
    }

    /**
     * 获取压力的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierPress(String exceptionCode){
        if(StringUtils.equals(exceptionCode,ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        return null;
    }

    /**
     * 获取pm2.5的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierPm25(String exceptionCode){
        if(StringUtils.equals(exceptionCode,ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        return null;
    }

    /**
     * 获取pm10的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierPm10(String exceptionCode){
        if(StringUtils.equals(exceptionCode,ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        return null;
    }

    /**
     * 获取甲醛的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierFormaldehyde(String exceptionCode){
        if(StringUtils.equals(exceptionCode,ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        return null;
    }
}
