package com.wx.somecode;

/**
 * @author xinquan.huangxq
 */
public class Counting {

    public static void main(String[] args) throws InterruptedException {
        final Counter counter = new Counter();

        class CountingThread extends Thread {
            public void run() {
                for (int x = 0; x < 100000; ++x) {
                    counter.increment();
                }
            }
        }

        CountingThread t1 = new CountingThread();
        CountingThread t2 = new CountingThread();
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(counter.getCount());
    }
}
