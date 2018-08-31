package com.wx.somecode;

/**
 * @author xinquan.huangxq
 */
@MyGetter
public class Main {

    private String value;

    public Main(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        Main app = new Main("it works");
        System.out.println(app.getValue());

        Order build = new OrderBuilder()
                .buildId(2)
                .buildAddTime(System.currentTimeMillis())
                .build();
        System.out.println(build);
    }
}
