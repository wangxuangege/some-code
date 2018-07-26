package com.wx.somecode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String code = createHelloWorldClassCode();
        ClassLoader classLoader = Main.class.getClassLoader();
        JavassistCompiler javassistCompiler = new JavassistCompiler();
        Class<?> helloWorldClass = javassistCompiler.compile(code, classLoader);
        Method method = helloWorldClass.getMethod("helloWorld", String.class);
        method.invoke(helloWorldClass.newInstance(), "wangxuan");
    }

    private static String createHelloWorldClassCode() {
        return "public class HelloWorld {\n" +
                "\t\n" +
                "\tpublic void helloWorld(String name) {\n" +
                "\t\tSystem.out.println(\"hello world, \" + name);\n" +
                "\t}\n" +
                "}";
    }
}
