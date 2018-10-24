package com.wx.somecode;

/**
 * @author xinquan.huangxq
 */
public class HelloWorld {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Hello from new thread");
            }
        };
        thread.start();

        Thread.yield();

        Thread.sleep(1000);

        System.out.println("Hello from main thread");
        thread.join();
    }
}
