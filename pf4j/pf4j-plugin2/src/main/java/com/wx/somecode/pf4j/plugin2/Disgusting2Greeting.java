package com.wx.somecode.pf4j.plugin2;

import com.wx.somecode.pf4j.Greeting;
import org.pf4j.Extension;

/**
 * @author xinquan.huangxq
 */
@Extension
public class Disgusting2Greeting implements Greeting {

    public String getGreeting() {
        return "Disgusting2";
    }
}
