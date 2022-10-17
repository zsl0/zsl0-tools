package com.zsl0.util.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author zsl0
 * create on 2022/10/17 16:53
 */
public class FutureUtil {

    Logger log = LoggerFactory.getLogger(FutureUtil.class);

    /**
     * 获取future结果，如果为null,创建空对象
     */
    public static <T> T futureGetObject(Future<T> future, Class<T> clazz) {
        T t = null;
        try {
            t = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        T t1 = null;
        try {
            t1 = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Objects.isNull(t) ? t1 : t;
    }


    /**
     * 获取future结果，如果为null,创建空对象
     */
    public static <T> List<T> futureGetList(Future<List<T>> future) {
        List<T> t = null;
        try {
            t = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<T> t1 = new ArrayList<>();
        return Objects.isNull(t) ? t1 : t;
    }
}
