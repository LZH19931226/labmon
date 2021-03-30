package com.hc.my.message.executor;

import com.hc.my.common.core.annotation.Monitor;

import java.util.concurrent.Future;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:01
 * 描述:
 **/
@Monitor
public interface NotifyExecutor {

    /**
     * Submit push task to executor queue.
     *
     * @param batchNo push batch number
     * @return synchronized execute result
     */
    Future<String> submit(String batchNo);
}
