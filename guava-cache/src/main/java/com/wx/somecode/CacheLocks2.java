package com.wx.somecode;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 与CacheLock相比，CacheLock2可以在多个线程调用加锁服务
 * 可以在lock地方卡主，在其他线程unlock
 */
public class CacheLocks2 {

    public static void main(String[] args) throws InterruptedException {
        LockDevice lockDevice = new LockDevice();
        int threadCount = 10;
        CountDownLatch cdl = new CountDownLatch(threadCount);

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            String threadName = "thread" + i;
            threads[i] = new Thread(() -> {
                lockDevice.lock("key");

                System.out.println("[" + threadName + "]开始运行了");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                System.out.println("[" + threadName + "]结束运行了");

                lockDevice.unlock("key");
                cdl.countDown();
            });
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }

        cdl.await();
    }

    private static class LockDevice {
        private LoadingCache<String, Semaphore> lockCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.SECONDS) // 10s没有被读取就会失效（发起连接所需时间肯定低于10s）
                .weakValues()
                .build(new CacheLoader<String, Semaphore>() {
                    @Override
                    public Semaphore load(String s) {
                        return new Semaphore(1);
                    }
                });

        void lock(String key) {
            try {
                Semaphore semaphore = lockCache.get(key);
                if (semaphore != null) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        // 理论上来说不会执行此处，没有地方中断调用
                    }
                }
            } catch (ExecutionException e) {
                // get返回为null会抛出此类异常，这种情况下不加锁处理
            }
        }

        /**
         * 执行unlock前必须lock，否则可能导致加锁逻辑异常
         *
         * @param key
         */
        void unlock(String key) {
            try {
                Semaphore semaphore = lockCache.get(key);
                semaphore.release();
            } catch (ExecutionException e) {
                // load返回为null会抛出此类异常，这种情况下不加锁处理
            }
        }
    }
}
