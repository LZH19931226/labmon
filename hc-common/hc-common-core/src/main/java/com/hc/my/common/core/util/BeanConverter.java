package com.hc.my.common.core.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 简单对象转换工具类
 *
 * @author
 */
@Slf4j
public class BeanConverter {


    public static <T, S> T to(S source, Class<T> t) {
        T target = null;
        if (source != null) {
            try {
                target = t.newInstance();
            } catch (Exception e) {
                log.error("实例化class类出现异常：%s", e);
            }
            BeanUtils.copyProperties(source, target);
        }
        return target;
    }


    public static <T, S> List<T> to(List<S> source, Class<T> t) {

        if (source == null) {
            return null;
        }
        List<T> targets = getTargetList(source.size());

        for (S s : source) {
            targets.add(to(s, t));
        }

        return targets;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> List<T> getTargetList(int size) {
        return new ArrayList(size);
    }


    public static <T, S> T convert(S source, Class<T> t) {
        T target = null;
        if (source != null) {
            try {
                JSON json = (JSON) JSON.toJSON(source);
                target = JSON.toJavaObject(json, t);
            } catch (Exception e) {
                log.error("获取page对象出现异常:%s", e);
            }
        }
        return target;
    }

    public static <T, S> List<T> convert(List<S> source, Class<T> t) {
        if (source == null) {
            return null;
        }

        List<T> targets = getTargetList(source.size());

        for (S s : source) {
            targets.add(convert(s, t));
        }
        return targets;
    }


}
