package com.hc.my.common.core.util;

import com.hc.my.common.core.constant.enums.ElkLogDetail;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElkLogDetailUtil {

    public static  void  buildElkLogDetail(ElkLogDetail elkLogDetail, String message, String logId){
     log.info("序列号:{}，序列id:{},序列描述:{},序列内容：{}",elkLogDetail.getCode(),logId,elkLogDetail.getMessage(),message);
    }
}
