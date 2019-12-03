package com.wx.somecode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xinquan.huangxq
 */
public class CircleC {

    @Getter
    @Setter
    private CircleA circleA;

    public CircleC(CircleA circleA) {
        this.circleA = circleA;
    }

    public CircleC() {}
}
