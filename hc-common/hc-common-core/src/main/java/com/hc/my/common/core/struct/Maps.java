package com.hc.my.common.core.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 10:11
 * 描述:
 **/
public class Maps extends HashMap<String, Object> {

    private static final long serialVersionUID = 1554544447626842801L;

    public Maps(Object... args) {
        if (null == args || args.length % 2 == 1) {
            throw new IllegalArgumentException("Illegal parameter.");
        }
        for (int index = 0; index < args.length; index = index + 2) {
            put(String.valueOf(args[index]), args[index + 1]);
        }
    }

    public Maps add(String key, Object valaue) {
        super.put(key, valaue);
        return this;
    }

    public Maps add(Map<String, Object> inner) {
        super.putAll(inner);
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public static <K, V> Map<K, V> of(K key, V value) {
        return (Map<K, V>) new Maps(key, value);
    }
}
