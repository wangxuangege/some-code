package com.wx.somecode;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xinquan.huangxq
 */
public class A {

    private static final AtomicInteger CNT = new AtomicInteger(1);

    @Getter
    private String str;

    public A() {
        this.str = "A@" + CNT.getAndAdd(1);
    }
}
