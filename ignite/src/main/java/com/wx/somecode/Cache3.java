package com.wx.somecode;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.transactions.Transaction;

public class Cache3 {

    /**
     * 事务操作
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start(Cache3.class.getClassLoader().getResource("example-ignite.xml"))) {
            IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCacheName2");
            cache.put("Hello", 0);

            try (Transaction tx = ignite.transactions().txStart()) {
                cache.put("Hello", 100);
                System.out.println(cache.get("Hello"));

                if (cache.get("Hello") == 100) {
                    tx.rollback();
                } else {
                    tx.commit();
                }
            } catch (Throwable e) {

            }

            System.out.println(cache.get("Hello"));
        }
    }
}
