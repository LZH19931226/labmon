package com.hc.labmanagent;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.vo.equimenttype.InstrumentmonitorVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = ApplicationName.LABMANAGENMENT)
public interface ProbeInfoApi {

    /**
     * 获取探头监控的信息
     * @return
     */
    @GetMapping("/instrumentparamconfig/getInstrumentMonitorInfo")
    ApiResponse<List<InstrumentmonitorVo>> selectInstrumentMonitorInfo();
}
