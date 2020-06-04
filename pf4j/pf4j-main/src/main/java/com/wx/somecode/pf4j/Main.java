package com.wx.somecode.pf4j;

import org.pf4j.*;

import java.util.List;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) {
        System.setProperty("pf4j.pluginsDir", "D:\\Workspace\\projects\\some-code\\pf4j\\pf4j-main\\target\\classes\\plugins");

        final PluginManager pluginManager = new DefaultPluginManager() {
            protected ExtensionFinder createExtensionFinder() {
                DefaultExtensionFinder extensionFinder = (DefaultExtensionFinder) super.createExtensionFinder();
                return extensionFinder;
            }
        };

        pluginManager.loadPlugins();

        pluginManager.startPlugins();

        List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);

        System.out.println(String.format("Found %d extensions for extension point '%s'", greetings.size(), Greeting.class.getName()));
        for (Greeting greeting : greetings) {
            System.out.println(">>> " + greeting.getGreeting());
        }
    }
}
