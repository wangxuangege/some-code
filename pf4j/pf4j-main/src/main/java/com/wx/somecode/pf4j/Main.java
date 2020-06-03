package com.wx.somecode.pf4j;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.util.List;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) {
        System.setProperty("pf4j.pluginsDir", "plugins");

        final PluginManager pluginManager = new DefaultPluginManager();

        pluginManager.loadPlugins();

        pluginManager.startPlugins();

        List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);
        System.out.println(String.format("Found %d extensions for extension point '%s'", greetings.size(), Greeting.class.getName()));
        for (Greeting greeting : greetings) {
            System.out.println(">>> " + greeting.getGreeting());
        }
    }
}
