package com.hc;

import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.domain.P2PNotify;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ApplicationName.MESSAGE)
public interface MessageApi {

    @PostMapping("/message/p2PNotify")
    ApiResponse<String> send(@RequestBody P2PNotify notify);

}

