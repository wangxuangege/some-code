package com.wx.somecode;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

public class Cache2 {

    /**
     * 原子操作
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start(Cache2.class.getClassLoader().getResource("example-ignite.xml"))) {
            IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCacheName2");

            Integer oldVal = cache.getAndPutIfAbsent("Hello", 11);
            System.out.println("old:" + oldVal + ",new:" + cache.get("Hello"));

            boolean success = cache.putIfAbsent("World", 22);
            System.out.println("success:" + success + ",val:" + cache.get("World"));

            oldVal = cache.getAndReplace("Hello", 11);
            System.out.println("old:" + oldVal + ",new:" + cache.get("Hello"));

            success = cache.replace("World", 22);
            System.out.println("success:" + success + ",val:" + cache.get("World"));

            success = cache.replace("World", 2, 22);
            System.out.println("success:" + success + ",val:" + cache.get("World"));

            success = cache.remove("Hello", 1);
            System.out.println("success:" + success + ",val:" + cache.get("Hello"));
        }
    }
}
