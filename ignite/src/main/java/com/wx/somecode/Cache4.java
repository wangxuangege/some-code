package com.wx.somecode;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.transactions.Transaction;

import java.util.concurrent.locks.Lock;

public class Cache4 {

    /**
     * 分布式锁
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start(Cache4.class.getClassLoader().getResource("example-ignite.xml"))) {
            IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCacheName2");

            Lock lock = cache.lock("Hello");
            lock.lock();
            try {
                cache.put("Hello", 11);
                cache.put("World", 22);
            } finally {
                lock.unlock();
            }

            System.out.println(cache.get("Hello"));
            System.out.println(cache.get("World"));
        }
    }
}
