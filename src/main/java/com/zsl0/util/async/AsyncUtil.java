package com.zsl0.util.async;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author zsl0
 * create on 2022/10/16 20:20
 */
public class AsyncUtil {

    static Logger log = LoggerFactory.getLogger(AsyncUtil.class);

    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() / 2 + 1,
                Runtime.getRuntime().availableProcessors() - 1,
                6, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        log.info("current processors: {}", Runtime.getRuntime().availableProcessors());

        asyncCompletableFuture(threadPool);

    }

    public static void asyncCompletableFuture(ThreadPoolExecutor threadPool) {
        CompletableFuture<String> objectCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return "100" + 1;
        });

        objectCompletableFuture.thenApply((a)->{
            System.out.println(a + 11);
            return a;
        }).thenAccept((a)->{
            System.out.println(a+"bbbbb");
        });

    }

    @Data
    public static class A {
        private String aName = "a";
    }

    @Data
    public static class B {
        private String bName = "a";
    }


    public static void asyncFuture(ThreadPoolExecutor threadPool) {
        Future<?> submit = threadPool.submit(() -> {
            System.out.println(Thread.currentThread().getName() + ":" + 123);
            try {
                Thread.sleep(3000);
                System.out.println("睡眠结束");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

//            return 100;
        });
        System.out.println("启动线程");
        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println("执行结束！");
    }

    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() / 2 + 1,
            Runtime.getRuntime().availableProcessors() + 1,
            6, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

//    public PriceResult getCheapestPlatAndPrice2(String product) {
//        Future<PriceResult> mouBaoFuture = threadPool.submit(() -> computeRealPrice(HttpRequestMock.getMouBaoPrice(product), HttpRequestMock.getMouBaoDiscounts(product)));
//        Future<PriceResult> mouDongFuture = threadPool.submit(() -> computeRealPrice(HttpRequestMock.getMouDongPrice(product), HttpRequestMock.getMouDongDiscounts(product)));
//        Future<PriceResult> mouXiXiFuture = threadPool.submit(() -> computeRealPrice(HttpRequestMock.getMouXiXiPrice(product), HttpRequestMock.getMouXiXiDiscounts(product)));
//
//        // 等待所有线程结果都处理完成，然后从结果中计算出最低价
//        return Stream.of(mouBaoFuture, mouDongFuture, mouXiXiFuture)
//                .map(priceResultFuture -> {
//                    try {
//                        return priceResultFuture.get(5L, TimeUnit.SECONDS);
//                    } catch (Exception e) {
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull).min(Comparator.comparingInt(PriceResult::getRealPrice)).get();
//    }
//
//    public static class PriceResult {
//
//    }
}
