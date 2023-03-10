package com.zsl0.util.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * @author zsl0
 * create on 2022/10/17 9:20
 */
public class ListUtil {

    static Logger log = LoggerFactory.getLogger(ListUtil.class);

    /**
     * 自定义List转Map，避免Stream.collect(toMap) key冲突时 java.lang.IllegalStateException: Duplicate key x.xxx异常
     */
    public static <T, K, V> Map<K, V> toMap(List<T> list, Function<T, K> key, Function<T, V> value) {
        Map<K, V> map = new HashMap<>(list.size());
        for (T data : list) {
            K mapKey = key.apply(data);
            V mapValue = value.apply(data);
            map.put(mapKey, mapValue);
        }

        return map;
    }

    /**
     * 自定义List转Map，key为Function返回，value为list元素
     */
    public static <T, K> Map<K, T> toMap(List<T> list, Function<T, K> key) {
        Map<K, T> map = new HashMap<>(list.size());
        for (T data : list) {
            K mapKey = key.apply(data);
            map.put(mapKey, data);
        }

        return map;
    }


    /**
     * 集合浅copy
     */
    public static <T> List<T> copy(List<T> source) {
        List<T> dest = new ArrayList<>();

        if (Objects.isNull(source)) {
            return dest;
        }

        dest.addAll(source);
        return dest;
    }

}
