package com.wx.somecode;

import org.drools.core.io.impl.ClassPathResource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.Collection;

/**
 * @author xinquan.huangxq
 */
public class Main {

    static KieSession getSession() {
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kb.add(new ClassPathResource("rule.drl"), ResourceType.DRL);
        Collection<KnowledgePackage> collection = kb.getKnowledgePackages();
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(collection);
        StatefulKnowledgeSession statefulSession = knowledgeBase.newStatefulKnowledgeSession();
        return statefulSession;
    }


    public static void run() {
        KieSession ks = getSession();

        Person p1 = new Person("白展堂", 68);
        Person p2 = new Person("李大嘴", 32);
        Person p3 = new Person("佟湘玉", 18);
        Person p4 = new Person("郭芙蓉", 8);
        Person p5 = new Person("祝无双", 66);

        System.out.println("before p1 : " + p1);
        System.out.println("before p2 : " + p2);
        System.out.println("before p3 : " + p3);
        System.out.println("before p4 : " + p4);

        ks.insert(p1);
        ks.insert(p2);
        ks.insert(p3);
        ks.insert(p4);
        ks.insert(p5);

        int count = ks.fireAllRules();

        System.out.println("总执行了" + count + "条规则------------------------------");
        System.out.println("after p1 : " + p1);
        System.out.println("after p2 : " + p2);
        System.out.println("after p3 : " + p3);
        System.out.println("after p4 : " + p4);
        System.out.println("after p4 : " + p5);
        ks.dispose();

    }

    public static void main(String[] args) {
        run();
    }
}
