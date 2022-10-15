package com.zsl0.util;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author zsl0
 * @date 2022/10/15 12:16
 */
@Slf4j
public class InsertUtil {

    /**
     * 批量插入
     *
     * @param consumer 插入函数
     * @param data 数据集合
     * @param pageSize 页大小
     * @param <T> 数据模板
     */
    public static <T> void insertBatch(Consumer<List<T>> consumer, List<T> data, Integer pageSize) {
        List<List<T>> page = new ArrayList<>();
        ListUtil.page(data, pageSize, page::add);

        for (List<T> insert : page) {
            consumer.accept(insert);
        }
    }
}
