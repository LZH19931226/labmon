package com.hc.my.common.core.spring;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 14:08
 * 描述:
 **/
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

public abstract class AbstractComponentCollector<T> implements ApplicationListener<ApplicationContextEvent> {

    /**
     * Spring容器启动完毕后回调
     */
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        getBeans(event)
                .filter(entry -> this.filter(entry.getValue()))
                .filter(entry -> entry.getValue() != this)
                .sorted((pre, cur) -> compare(pre.getValue(), cur.getValue()))
                .filter(entry -> CollectionUtils.isEmpty(classes()) ||
                        classes().stream().anyMatch(c -> c == entry.getValue().getClass()))
                .forEach(entry -> this.collect(entry.getKey(), entry.getValue()));
    }

    /**
     * 获取指定的组件.
     *
     * @param event 应用启动上下文事件
     * @return 结果集
     */
    @SuppressWarnings({"unchecked"})
    private Stream<Entry<String, T>> getBeans(ApplicationContextEvent event) {
        if (null != getInterface() && Object.class != getInterface()) {
            return event.getApplicationContext().getBeansOfType(getInterface()).entrySet().stream();
        }
        return annotations()
                .stream()
                .flatMap(pc -> event.getApplicationContext().getBeansWithAnnotation(pc).entrySet().stream())
                .map(entry -> (Entry<String, T>) entry);
    }

    /**
     * 获取泛型接口中的参数类型.
     *
     * @return 请求报文类型
     */
    @SuppressWarnings({"unchecked"})
    private Class<T> getInterface() {
        Type type = this.getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException(this.getClass().getName() + " must appointed generic type.");
        }
        ParameterizedType pType = (ParameterizedType) type;
        if (null == pType.getActualTypeArguments() || pType.getActualTypeArguments().length == 0) {
            throw new IllegalArgumentException(this.getClass().getName() + " must appointed generic type.");
        }
        return (Class<T>) pType.getActualTypeArguments()[0];
    }

    /**
     * 排序组件.
     *
     * @param pre 被比较
     * @param cur 比较
     * @return 比较结果
     */
    private int compare(Object pre, Object cur) {
        Order po = pre.getClass().getAnnotation(Order.class);
        Order co = cur.getClass().getAnnotation(Order.class);
        if (null == po && null == co) {
            return 0;
        }
        if (null == po) {
            return 1;
        }
        if (null == co) {
            return -1;
        }
        return Integer.compare(po.value(), co.value());
    }

    /**
     * 当前SpringBean是否支持排序处理.
     *
     * @param bean 当前SpringBean
     * @return 是否支持
     */
    protected boolean filter(Object bean) {
        return true;
    }

    /**
     * 接口或者类上的注解.
     *
     * @return 注解结合
     */
    protected List<Class<? extends Annotation>> annotations() {
        return Collections.emptyList();
    }

    /**
     * 要组合的类.如果不为空，会只组合该列表的返回的处理器.
     *
     * @return 处理器类
     */
    protected List<Class<?>> classes() {
        return Collections.emptyList();
    }

    /**
     * 组件搜集完成回调.
     *
     * @param name      组件名
     * @param component 排序后的业务组件
     */
    protected abstract void collect(String name, T component);
}
