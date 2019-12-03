package com.wx.somecode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xinquan.huangxq
 */
public class CircleA {

    @Getter
    @Setter
    private CircleB circleB;

    public CircleA(CircleB circleB) {
        this.circleB = circleB;
    }

    public CircleA() {}
}
