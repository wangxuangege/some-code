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
                .expireAfterAccess(1, TimeUnit.SECONDS)
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
        }, 200, 200, TimeUnit.MILLISECONDS);


        for (int i = 0; i < 5; ++i) {
            cache.put(i, i);
        }
        cache.invalidate(2);
        cache.invalidate(4);

        Thread.sleep(2000L);

        System.out.println(cache.getIfPresent(3));
        executor.shutdown();
        scheduledExecutorService.shutdown();
    }
}
