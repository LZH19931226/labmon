package com.hc.my.common.core.probe;


import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * MT400新的
 *
 * @author user
 */
@Data
public class MT400New extends PublicMethod{

    /**温度*/
    private String temperature;

    /**湿度*/
    private String humidity;

    /**二氧化碳*/
    private String co2;

    /**氧气*/
    private String o2;

    /**气压*/
    private String press;

    /**pm2.5*/
    private String pm25;

    /**pm10*/
    private String pm10;

    /**甲醛*/
    private String formaldehyde;

    /**空气质量*/
    private String voc;

    /**ups*/
    public String ups;

    /**电压*/
    private String voltage;

    /**
     * 获取温度的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     *  获取湿度的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierHumidity(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.F1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.F0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取二氧化碳的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCo2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.F1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.F0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取氧气的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierO2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF2.name())) {
            return ProbeOutlier.NO_CALIBRATION.name();
        }
        return null;
    }

    /**
     * 获取气压的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierPress(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取pm2.5的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierPm25(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取pm10的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierPm10(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取甲醛的的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierFormaldehyde(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取空气质量的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierVoc(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取ups的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierUps(String exceptionCode){
        return getUpsInfo(exceptionCode);
    }

    /**
     * 电压物异常信息
     * @param exceptionCode
     * @return null
     */
    public String getProbeOutlierVoltage(String exceptionCode){
        return null;
    }
}
