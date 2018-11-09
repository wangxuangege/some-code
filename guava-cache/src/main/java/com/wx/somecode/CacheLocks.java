package com.wx.somecode;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CacheLocks {

    public static void main(String[] args) throws InterruptedException {
        LockCache cache = new LockCache();
        int threadCount = 10;
        CountDownLatch cdl = new CountDownLatch(threadCount);

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            String threadName = "thread" + i;
            threads[i] = new Thread(() -> {
                ReentrantLock lock = cache.get("key");
                if (lock == null) {
                    System.out.println("[" + threadName + "]获取锁失败");
                    return;
                }
                lock.lock();

                System.out.println("[" + threadName + "]开始运行了");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                System.out.println("[" + threadName + "]结束运行了");

                lock.unlock();
                cdl.countDown();
            });
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }

        cdl.await();
    }

    private static class LockCache {
        private LoadingCache<String, ReentrantLock> lockCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.SECONDS) // 10s没有被读取就会失效（发起连接所需时间肯定低于10s）
                .weakValues()
                .build(new CacheLoader<String, ReentrantLock>() {
                    @Override
                    public ReentrantLock load(String s) {
                        return new ReentrantLock();
                    }
                });

        ReentrantLock get(String key) {
            try {
                return lockCache.get(key);
            } catch (ExecutionException e) {
                return null;
            }
        }
    }
}
