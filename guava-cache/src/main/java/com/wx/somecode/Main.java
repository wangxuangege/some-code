package com.wx.somecode;

import com.google.common.cache.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Cache<Integer, Integer> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .removalListener(RemovalListeners.asynchronous(notification -> {
                    System.out.println(notification.getCause());
                    System.out.println(notification.getKey() + " --> " + notification.getValue());
                }, executor))
                .build();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                cache.cleanUp();
            }
        }, 1, 1, TimeUnit.SECONDS);


        for (int i = 0; i < 5; ++i) {
            cache.put(i, i);
        }
        cache.invalidate(2);

        // 测试get=null后是否会触发expire事件
        Integer val;
        while ((val = cache.getIfPresent(3)) != null) {
            System.out.println(val);
            Thread.sleep(56);
        }
        System.out.println(val);

        Thread.sleep(3000);

        executor.shutdown();
        scheduledExecutorService.shutdown();
    }
}
