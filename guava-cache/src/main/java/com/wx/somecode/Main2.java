package com.wx.somecode;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xinquan.huangxq
 */
public class Main2 {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
        Cache cache = (Cache) ctx.getBean("cacheTest");

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                //cache.evictExpiredElements();
            }
        }, 200, 200, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 5; ++i) {
            cache.put(new Element(i, i));
        }
        cache.remove(2);
        cache.remove(4);

        Thread.sleep(2000L);

        System.out.println(cache.get(3));

        scheduledExecutorService.shutdown();
    }
}
