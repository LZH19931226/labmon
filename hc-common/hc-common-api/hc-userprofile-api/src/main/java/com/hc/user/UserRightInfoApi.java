package com.hc.user;

import com.hc.my.common.core.bean.ApplicationName;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ApplicationName.USER)
public interface UserRightInfoApi {

    /**
     * 查询当前医院所有的人员信息
     * @param hospitalCode 医院id
     * @return
     */
    @GetMapping("/userRight/getALLHospitalUserRightInfo")
    List<UserRightRedisDto> findALLUserRightInfoByHC(@RequestParam("hospitalCode")String hospitalCode);
}
