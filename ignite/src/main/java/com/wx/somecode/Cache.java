package com.wx.somecode;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

public class Cache {

    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start(Cache.class.getClassLoader().getResource("example-ignite.xml"))) {
            IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCacheName");
            for (int i = 0; i < 10; i++)
                cache.put(i, Integer.toString(i));
            for (int i = 0; i < 10; i++)
                System.out.println("Got [key=" + i + ", val=" + cache.get(i) + ']');
        }
    }
}
