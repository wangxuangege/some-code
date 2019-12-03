package com.wx.somecode;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
        A a1 = (A) ctx.getBean("a");
        A a2 = (A) ctx.getBean("tmpA");
        A a3 = (A) ctx.getBean("fA");
        System.out.println(a1.getStr());
        System.out.println(a2.getStr());
        System.out.println(a3.getStr());

        A b1 = (A) ctx.getBean("b");
        A b2 = (A) ctx.getBean("b");
        System.out.println(b1.getStr());
        System.out.println(b2.getStr());

        A c1 = (A) ctx.getBean("c");
        A c2 = (A) ctx.getBean("c");
        System.out.println(c1.getStr());
        System.out.println(c2.getStr());

        A ca1 = (A) ctx.getBean("ca");
        A ca2 = (A) ctx.getBean("ctmpA");
        A ca3 = (A) ctx.getBean("fA");
        System.out.println(ca1.getStr());
        System.out.println(ca2.getStr());
        System.out.println(ca3.getStr());

        Object bFactory = ctx.getBean("&b");
        System.out.println(bFactory.getClass());

        A sa = (A) ctx.getBean("sa");
        System.out.println(sa.getStr());

        A f1 = (A) ctx.getBean("f");
        A f2 = (A) ctx.getBean("f");
        System.out.println(f1.getStr());
        System.out.println(f2.getStr());

        try {
            System.out.println("构造器循环依赖");
            new ClassPathXmlApplicationContext("spring-context-circle.xml");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        try {
            System.out.println("Setter循环依赖");
            ClassPathXmlApplicationContext ctx2 = new ClassPathXmlApplicationContext("spring-context-circle2.xml");
            ctx2.getBean("testA2");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        ctx.close();
    }
}
