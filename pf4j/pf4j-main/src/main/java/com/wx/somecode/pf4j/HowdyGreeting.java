package com.wx.somecode.pf4j;

/**
 * A Service Implementation (no @Extension) declared via Java Service Provider mechanism (using META-INF/services).
 */
public class HowdyGreeting implements Greeting {

    public String getGreeting() {
        return "Howdy";
    }

}
