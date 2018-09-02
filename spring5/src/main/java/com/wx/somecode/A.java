package com.wx.somecode;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author xinquan.huangxq
 */
public class A implements ResourceLoaderAware {

    @Getter
    private ResourceLoader resourceLoader;

    @Setter
    @Getter
    private Resource resource;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
