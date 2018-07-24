package com.wx.somecode;

import javassist.*;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CountServiceImpl delegate = new CountServiceImpl();

        long time = System.currentTimeMillis();
        CountService jdkDynamicProxy = createJdkDynamicProxy(delegate);
        System.out.println("Create Jdk Proxy : " + (System.currentTimeMillis() - time) + " ms");

        time = System.currentTimeMillis();
        CountService cglibDynamicProxy = createCglibDynamicProxy(delegate);
        System.out.println("Create Cglib Proxy : " + (System.currentTimeMillis() - time) + " ms");

        time = System.currentTimeMillis();
        CountService springCglibDynamicProxy = createSpringCglibDynamicProxy(delegate);
        System.out.println("Create Spring Cglib Proxy : " + (System.currentTimeMillis() - time) + " ms");

        time = System.currentTimeMillis();
        CountService javassistProxy = createJavassistDynamicProxy(delegate);
        System.out.println("Create Javassist Proxy : " + (System.currentTimeMillis() - time) + " ms");

        time = System.currentTimeMillis();
        CountService javassistBytecodeProxy = createJavassistBytecodeDynamicProxy(delegate);
        System.out.println("Create Javassist Bytecode Proxy : " + (System.currentTimeMillis() - time) + " ms");

        test(jdkDynamicProxy, "Run jdk proxy:");
        test(cglibDynamicProxy, "Run cglib proxy:");
        test(springCglibDynamicProxy, "Run spring cglib proxy:");
        test(javassistProxy, "Run javassist proxy:");
        test(javassistBytecodeProxy, "Run javassist bytecode proxy:");
    }

    private static CountService createJavassistBytecodeDynamicProxy(CountServiceImpl delegate) throws Exception {
        ClassPool mPool = new ClassPool(true);
        CtClass mCtc = mPool.makeClass(CountService.class.getName() + "JavaassistProxy");

        mCtc.addInterface(mPool.get(CountService.class.getName()));
        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
        mCtc.addField(CtField.make("public " + CountService.class.getName() + " delegate;", mCtc));
        mCtc.addMethod(CtNewMethod.make("public int count() { return delegate.count(); }", mCtc));
        Class pc = mCtc.toClass();
        CountService byteCodeProxy = (CountService) pc.newInstance();
        Field filed = byteCodeProxy.getClass().getField("delegate");
        filed.set(byteCodeProxy, delegate);
        return byteCodeProxy;
    }

    public static void test(CountService service, String label) {
        service.count();
        int count = 10000000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            service.count();
        }
        time = System.currentTimeMillis() - time;
        System.out.println(label + time + " ms," + new DecimalFormat().format(count * 1000 / time) + " t/s");
    }

    private static CountService createJdkDynamicProxy(final CountService delegate) {
        CountService jdkProxy = (CountService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{CountService.class}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(delegate, args);
            }
        });
        return jdkProxy;
    }

    private static CountService createCglibDynamicProxy(final CountService delegate) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                return method.invoke(delegate, args);
            }
        });
        enhancer.setInterfaces(new Class[]{CountService.class});
        CountService proxy = (CountService) enhancer.create();
        return proxy;
    }

    private static CountService createSpringCglibDynamicProxy(final CountService delegate) {
        org.springframework.cglib.proxy.Enhancer springEnhancer = new org.springframework.cglib.proxy.Enhancer();
        springEnhancer.setCallback(new org.springframework.cglib.proxy.MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] args, org.springframework.cglib.proxy.MethodProxy methodProxy) throws Throwable {
                return method.invoke(delegate, args);
            }
        });
        springEnhancer.setInterfaces(new Class[]{CountService.class});
        CountService proxy = (CountService) springEnhancer.create();
        return proxy;
    }

    private static CountService createJavassistDynamicProxy(final CountService delegate) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(new Class[]{CountService.class});
        Class proxyClass = proxyFactory.createClass();
        CountService javassistProxy = (CountService) proxyClass.newInstance();
        ((ProxyObject) javassistProxy).setHandler(new MethodHandler() {
            public Object invoke(Object o, Method method, Method method1, Object[] args) throws Throwable {
                return method.invoke(delegate, args);
            }
        });
        return javassistProxy;
    }

    public interface CountService {
        int count();
    }

    public static class CountServiceImpl implements CountService {
        private int count = 0;

        public int count() {
            return count++;
        }
    }
}
