package com.wx.somecode;

import static com.wx.somecode.MyMockito.mock;
import static com.wx.somecode.MyMockito.when;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) {
        MyInterface calculator = mock(MyInterface.class);
        when(calculator.f(1, 2)).thenReturn(3);
        when(calculator.f(1, 3)).thenReturn(4);

        System.out.println(calculator.f(1, 2));
        System.out.println(calculator.f(1, 3));
        System.out.println(calculator.f(1, 2));
        // 没有mock就返回0
        System.out.println(calculator.f(2, 2));
    }

    interface MyInterface {
        int f(int a, int b);
    }
}
