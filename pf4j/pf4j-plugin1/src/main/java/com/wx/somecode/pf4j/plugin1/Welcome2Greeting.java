package com.wx.somecode.pf4j.plugin1;

import com.wx.somecode.pf4j.Greeting;
import org.pf4j.Extension;

/**
 * @author xinquan.huangxq
 */
@Extension
public class Welcome2Greeting implements Greeting {

    public String getGreeting() {
        return "Welcome2";
    }
}
