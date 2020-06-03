package com.wx.somecode.pf4j;

import org.pf4j.ExtensionPoint;

/**
 * @author xinquan.huangxq
 */
public interface Greeting extends ExtensionPoint {

    String getGreeting();
}
