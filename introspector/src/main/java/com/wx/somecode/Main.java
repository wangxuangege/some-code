package com.wx.somecode;

import com.alibaba.fastjson.JSON;

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, IntrospectionException, NoSuchMethodException {
        reflect();
        introspector();
    }

    public static void reflect() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        Field f = user.getClass().getDeclaredField("name");
        f.setAccessible(true);
        f.set(user, "mld");//设置属性值
        String name = (String) f.get(user);//获取属性值
        System.out.println(name);
    }

    public static void introspector() throws InvocationTargetException, IllegalAccessException, IntrospectionException, NoSuchMethodException {
        User user = new User();
        user.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(evt.getPropertyName() + ":" + evt.getOldValue() + "--->>>" + evt.getNewValue());
            }
        });

        //操作单个属性
        PropertyDescriptor pd = new PropertyDescriptor("name", User.class);
        Method w = pd.getWriteMethod();//获取属性的setter方法
        w.invoke(user, "winclpt");
        Method r = pd.getReadMethod();//获取属性的getter方法
        System.out.println(r.invoke(user, null));

        PropertyDescriptor pd2 = new PropertyDescriptor("a", User.class, "getAAA", "setAAA");
        Method w2 = pd2.getWriteMethod();
        w2.invoke(user, "ddd");
        Method r2 = pd2.getReadMethod();
        System.out.println(r2.invoke(user, null));
    }
}
