package com.wx.somecode.pf4j;

import org.pf4j.Extension;

@Extension
public class WhazzupGreeting implements Greeting {

    public String getGreeting() {
        return "Whazzup";
    }

}
