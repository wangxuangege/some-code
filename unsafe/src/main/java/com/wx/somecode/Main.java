package com.wx.somecode;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Main {

    /**
     * 根据设置USE_UNSAFE的值体验一下CAS的实现
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final MyAtomInt myInt = new MyAtomInt(0);

        Thread thread = new Thread() {
            public void run() {
                for (int i = 0; i < 1000000; ++i) {
                    myInt.getAndIncrement();
                }
            }
        };
        Thread thread2 = new Thread() {
            public void run() {
                for (int i = 0; i < 1000000; ++i) {
                    myInt.getAndIncrement();
                }
            }
        };

        thread.start();
        thread2.start();

        thread.join();
        thread2.join();

        System.out.println(myInt.get());
    }

    /**
     * 第一种：
     * -Xbootclasspath/a:${path}
     *
     * @return
     */
    public static Unsafe getUnsafe1() {
        return Unsafe.getUnsafe();
    }

    public static Unsafe getUnsafe2() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    static class MyAtomInt {
        private static final Unsafe UNSAFE = getUnsafe2();

        private static final long VALUE_OFFSET = getValueOffset();

        private static final boolean USE_UNSAFE = true;

        private static long getValueOffset() {
            try {
                return UNSAFE.objectFieldOffset(MyAtomInt.class.getDeclaredField("value"));
            } catch (NoSuchFieldException e) {
                throw new Error(e);
            }
        }

        private volatile int value;

        public MyAtomInt(int initialValue) {
            value = initialValue;
        }

        public final int getAndIncrement() {
            return USE_UNSAFE ? UNSAFE.getAndAddInt(this, VALUE_OFFSET, 1) : value++;
        }

        public final int get() {
            return value;
        }
    }
}
