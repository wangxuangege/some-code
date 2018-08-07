package com.wx.somecode;

import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

/**
 * @author xinquan.huangxq
 */
public class RuleTest {

    @Test
    public void test1() throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }

        Rule c = new Rule() {
            {
                setId(2);
            }

            public long getId() {
                return 10;
            }
        };

        System.out.println(constructor.newInstance(Rule.class,
                MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(Rule.class.getMethod("getId"), Rule.class).bindTo(c).invokeWithArguments());

        System.out.println(constructor.newInstance(c.getClass(),
                MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(Rule.class.getMethod("getId"), c.getClass()).bindTo(c).invokeWithArguments());

        System.out.println(c.getId());

        System.out.println(constructor.newInstance(c.getClass(),
                MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(c.getClass().getMethod("getId"), c.getClass()).bindTo(c).invokeWithArguments());
    }

    @Test
    public void test2() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findStatic(RuleTest.class, "doubleVal", MethodType.methodType(int.class, int.class));
        List<Integer> dataList = Arrays.asList(1, 2, 3, 4, 5);
        RuleTest.transform(dataList, mh);
        for (Integer data : dataList) {
            System.out.println(data);
        }
    }

    public static List<Integer> transform(List<Integer> dataList, MethodHandle handle) throws Throwable {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.set(i, (Integer) handle.invoke(dataList.get(i)));
        }
        return dataList;
    }

    public static int doubleVal(int val) {
        return val * 2;
    }


}