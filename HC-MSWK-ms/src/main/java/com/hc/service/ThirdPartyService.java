package com.hc.service;

import com.hc.model.MapperModel.TimeoutEquipment;

/**
 * @author LiuZhiHao
 * @date 2020/7/9 9:28
 * 描述: 第三方医院需求
 **/
public interface ThirdPartyService {
    /*
     * 功能描述: 上海仁济超时报警
     * 〈〉
     * @Param:
     * @Return:
     * @Author: LiuZhiHao
     * @Date: 2020/7/9 9:28
     */
     void disableAlarm(TimeoutEquipment timeoutEquipment);
}
