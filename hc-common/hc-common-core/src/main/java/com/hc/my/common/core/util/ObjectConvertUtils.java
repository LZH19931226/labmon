package com.hc.my.common.core.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectConvertUtils {
    /**
     * 对象转map
     * @param obj
     * @return
     */
    public static Map<String, Object> getObjectToMap(Object obj)  {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String keyName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (value == null) {
                value = "";
            }
            if(value instanceof Date){
                value = DateUtils.paseDatetime((Date) value);
            }
            map.put(keyName, value);
        }
        return map;
    }
}
