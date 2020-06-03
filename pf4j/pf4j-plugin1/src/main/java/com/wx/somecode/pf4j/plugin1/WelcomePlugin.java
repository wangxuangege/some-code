package com.wx.somecode.pf4j.plugin1;


import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

/**
 * @author xinquan.huangxq
 */
public class WelcomePlugin extends Plugin {

    public WelcomePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("WelcomePlugin.start()");
    }

    @Override
    public void stop() {
        System.out.println("WelcomePlugin.stop()");
    }
}
