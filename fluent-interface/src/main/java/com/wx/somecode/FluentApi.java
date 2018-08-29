package com.wx.somecode;

import java.lang.reflect.Proxy;

/**
 * @author xinquan.huangxq
 */
public class FluentApi<T> {
    /**
     * 目标接口
     */
    private Class<T> target;

    private FluentApi(Class<T> target) {
        if (!target.isInterface()) {
            throw new IllegalArgumentException("target必须是个接口");
        }
        this.target = target;
    }

    public static <T> FluentApi<T> target(Class<T> target) {
        return new FluentApi<>(target);
    }

    @SuppressWarnings("unchecked")
    public T create() {
        JdkProxy jdkProxy = new JdkProxy(target);
        return (T) Proxy.newProxyInstance(target.getClassLoader(),
                new Class[]{target},
                jdkProxy);
    }
}