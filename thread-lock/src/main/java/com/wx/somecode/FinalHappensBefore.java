package com.wx.somecode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xinquan.huangxq
 */
public class FinalHappensBefore {

    public static void main(final String[] args) throws InterruptedException {

        final SafeStates array[] = new SafeStates[1];

        Thread t1 = new Thread() {
            public void run() {
                System.out.println("t1 start");

                array[0] = new SafeStates();

                System.out.println("t1 end");
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                System.out.println("t2 start");
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                }
                if (array[0] != null) {
                    System.out.println(array[0].get("A"));
                    System.out.println(array[0].get("C"));
                }
                System.out.println("t2 end");
            }
        };


        t2.start();
        t1.start();

        t1.join();
        t2.join();
    }

    public static class SafeStates {

        private final Map<String, String> states;

        public SafeStates() {
            states = new HashMap<String, String>();
            states.put("A", "1");
            states.put("B", "2");

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
            }

            states.put("C", "3");
            states.put("D", "4");
            states.put("E", "5");
        }

        public String get(String key) {
            return states.get(key);
        }
    }
}
