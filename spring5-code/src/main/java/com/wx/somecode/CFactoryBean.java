package com.wx.somecode;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author xinquan.huangxq
 */
public class CFactoryBean implements FactoryBean<A> {

    @Override
    public A getObject() throws Exception {
        return new A();
    }

    @Override
    public Class<?> getObjectType() {
        return A.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
