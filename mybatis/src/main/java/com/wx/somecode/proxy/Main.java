package com.wx.somecode.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Interface target = (Interface) Proxy.newProxyInstance(Main.class.getClassLoader(), new Class[] { Interface.class }, new ProxyInvocationHandler());
        System.out.println(target.toString());
        target.print();
        target.print2();
    }
}
