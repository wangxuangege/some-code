package com.wx.somecode;

/**
 * @author xinquan.huangxq
 */
public class Puzzle {

    private static boolean answerReader = false;

    private static int answer = 0;

    private static Thread t1 = new Thread() {

        public void run() {
            answer = 42;
            answerReader = true;
        }
    };

    private static Thread t2 = new Thread() {

        public void run() {
            if (answerReader) {
                System.out.println("The meaning of life is :" + answer);
            } else {
                System.out.println("I don't know the answer");
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        t1.start();

        Thread.sleep(1000);

        t2.start();
        t1.join();
        t2.join();
    }
}
