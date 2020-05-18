package com.wx.somecode.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author xinquan.huangxq
 */
public class MonitorAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new MonitorClassFileTransformer());
    }
}
