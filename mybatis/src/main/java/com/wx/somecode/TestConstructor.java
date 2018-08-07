package com.wx.somecode;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;

/**
 * @author xinquan.huangxq
 */
public class TestConstructor {

    public static void main(String[] args) throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }

        Rule c = new Rule() {{
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
}
