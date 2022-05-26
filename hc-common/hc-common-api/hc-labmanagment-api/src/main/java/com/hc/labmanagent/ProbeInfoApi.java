package com.hc.labmanagent;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.InstrumentmonitorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ApplicationName.LABMANAGENMENT)
public interface ProbeInfoApi {

    /**
     * 获取探头监控的信息
     * @return
     */
    @GetMapping("/instrumentparamconfig/getInstrumentMonitorInfo")
    ApiResponse<List<InstrumentmonitorDto>> selectInstrumentMonitorInfo(@RequestParam("hospitalCode") String hospitalCode);


    /**
     * 更新最新一次的报警时间(用于每小时只报警一次)
     * @param instrumentParamConfigNo 探头检测信息id
     * @param warningTime 报警时间
     */
    @PutMapping("/instrumentparamconfig/editProbeWarningTime")
    void editWarningTime(@RequestParam("instrumentParamConfigNo")String instrumentParamConfigNo,
                         @RequestParam("warningTime") String warningTime);
}
