package com.hc.warning;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.WarningrecordRedisInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationName.REDIS,decode404 = true)
public interface WarningApi {

    @PostMapping("/wr/add")
    void add(@RequestBody WarningrecordRedisInfo warningrecordRedisInfo);

    @GetMapping("/wr/getSize")
    ApiResponse<Long> getWarningRecordSize(@RequestParam("listCode")String listCode);

    @GetMapping("/wr/getLeftPopWarningRecord")
    ApiResponse<WarningrecordRedisInfo> getLeftPopWarningRecord(@RequestParam("listCode")String listCode);
}
