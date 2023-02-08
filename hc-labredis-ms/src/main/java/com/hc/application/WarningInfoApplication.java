package com.hc.application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hc.application.config.RedisUtils;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.dto.WarningrecordRedisInfo;
import com.hc.my.common.core.redis.namespace.MswkServiceEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class WarningInfoApplication {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 新增和更新报警信息
     * @param warningrecordRedisInfo
     */
    public void add(WarningrecordRedisInfo warningrecordRedisInfo) {
        //将warningrecord数据存入redis
        redisUtils.lSet(MswkServiceEnum.WARNING_RECORD.getCode(), JSON.toJSONString(warningrecordRedisInfo));
    }

    /***
     * 获取长度
     * @param listCode
     * @return
     */
    public Long getWarningRecordSize(String listCode) {
        long l = redisUtils.lGetListSize(listCode);
        System.out.println(l);
        return  redisUtils.lGetListSize(listCode);
    }

    /**
     * 获取并删除
     * @param listCode
     * @return
     */
    public WarningrecordRedisInfo getLeftPopWarningRecord(String listCode) {
        Object object = redisUtils.lLeftPop(listCode);
        if (null==object){
            return  null;
        }
        return JSON.parseObject((String)object, new TypeReference<WarningrecordRedisInfo>(){});
    }
}
