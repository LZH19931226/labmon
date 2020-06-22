package com.hc.service;

import com.hc.entity.Monitorinstrument;

import java.util.Date;

/**
 * Created by xxf on 2018/9/28.
 */
public interface CurrentDataService {
    /**
     *    将当前值按照监控类型分类存储进redis
     * @param data
     * @param monitorinstrument
     * @param type
     */
    void getCurrentData(String data, Monitorinstrument monitorinstrument, String type, Date date);
}
