package com.hc.my.common.core.util;

import java.lang.reflect.Field;
import java.util.*;

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

    public static void filterMap(Map<String, Object> objectMap, List<String> list) {
        list.add("inputdatetime");
        Iterator<String> iterator = objectMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if(!list.contains(next)){
                iterator.remove();
                objectMap.remove(next);
            }
        }
        list.remove("inputdatetime");
    }


    public static String getKey(Map<Object,Object> map, String v) {
        Set<Map.Entry<Object, Object>> set = map.entrySet();
        Iterator iterator=set.iterator();
        String key = "";
        while (iterator.hasNext()) {
            Map.Entry<String, Object> enter = (Map.Entry<String, Object>) iterator.next();
            if (enter.getValue().equals(v)) {
                key = enter.getKey();
            }}
        return key;
    }

    public static void main(String[] agrs){
        HashMap<Object,Object> map  = new HashMap<>();
        map.put("sadasd","sddadasd");
        map.put("111","sddad1asd");
        Object sddad1asd = getKey(map, "sddad1asd");
        System.out.println(sddad1asd);

    }

}
