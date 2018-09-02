package com.wx.somecode;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author xinquan.huangxq
 */
public class ResourceLoaderAwareMain {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
        A a = ctx.getBean(A.class);
        System.out.println(a.getResourceLoader());

        try (InputStream is = a.getResource().getInputStream()) {
            System.out.println(FileCopyUtils.copyToString(new InputStreamReader(is, "UTF-8")));
        }
    }
}
