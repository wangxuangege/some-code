package com.wx.somecode.proxy;

/**
 * @author xinquan.huangxq
 */
public interface Interface {

    void print();

    default void print2() {
        System.out.println("print2");
    }
}
