package com.hc.utils;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.po.Monitorinstrument;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/8/7 10:40
 * 描述:
 **/
@Component
public class ShowModelUtils {



    public WarningAlarmDo procWarnModel(String data, Monitorinstrument monitorinstrument, Date date, Integer instrumentconfigid, String unit) {
        WarningAlarmDo WarningAlarmDo = new WarningAlarmDo();
        WarningAlarmDo.setCurrrentData(data);
        WarningAlarmDo.setDate(date);
        WarningAlarmDo.setInstrumentconfigid(instrumentconfigid);
        WarningAlarmDo.setMonitorinstrument(BeanConverter.convert(monitorinstrument, MonitorinstrumentDo.class));
        WarningAlarmDo.setUnit(unit);
        WarningAlarmDo.setSn(monitorinstrument.getSn());
        return WarningAlarmDo;
    }
    /**
     * 值校准
     *
     * @param data
     * @return
     */
    public String calibration(InstrumentInfoDto instrumentInfoDto, String data) {
        //如果是异常值则直接返回异常值
        if (!RegularUtil.checkContainsNumbers(data)) {
            return data;
        }
        String calibration = instrumentInfoDto.getCalibration();
        if (StringUtils.isEmpty(calibration)) {
            return data;
        }
        //去除空
        String s = calibration.replaceAll(" ", "");
        try {
            return new BigDecimal(data).add(new BigDecimal(s)).toString();
        } catch (Exception e) {
            return data;
        }
    }


}
