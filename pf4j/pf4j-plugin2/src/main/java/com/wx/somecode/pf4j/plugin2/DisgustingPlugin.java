package com.wx.somecode.pf4j.plugin2;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

/**
 * @author xinquan.huangxq
 */
public class DisgustingPlugin extends Plugin {

    public DisgustingPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("DisgustingPlugin.start()");
    }

    @Override
    public void stop() {
        System.out.println("DisgustingPlugin.stop()");
    }
}
