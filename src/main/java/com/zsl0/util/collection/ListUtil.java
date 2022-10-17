package com.zsl0.util.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

}
