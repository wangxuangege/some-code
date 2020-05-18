package com.wx.somecode.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author xinquan.huangxq
 */
public class MonitorAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("Run MonitorAgent");

        instrumentation.addTransformer(new MonitorTransformer());
    }
}
