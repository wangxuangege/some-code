package com.wx.somecode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xinquan.huangxq
 */
public class CircleB {

    @Getter
    @Setter
    private CircleC circleC;

    public CircleB(CircleC circleC) {
        this.circleC = circleC;
    }

    public CircleB() {}
}
