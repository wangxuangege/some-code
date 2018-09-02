package com.wx.somecode;

import org.springframework.core.io.*;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author xinquan.huangxq
 */
public class ResourceMain {

    public static void main(String[] args) throws IOException {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        resourceLoader.addProtocolResolver(new ProtocolResolver() {
            @Nullable
            public Resource resolve(String location, ResourceLoader resourceLoader) {
                if (location.startsWith("rpath://")) {
                    return new ClassPathResource(location.substring("rpath://".length()));
                }
                return null;
            }
        });
        Resource a = resourceLoader.getResource("rpath://a.txt");
        try (InputStream is = a.getInputStream()) {
            System.out.println(FileCopyUtils.copyToString(new InputStreamReader(is, "UTF-8")));
        }
    }
}
