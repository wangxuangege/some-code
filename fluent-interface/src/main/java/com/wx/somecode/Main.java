package com.wx.somecode;

import com.alibaba.fastjson.JSON;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) {
        Student student = FluentApi.target(StudentBuilder.class)
                        .create()
                        .age(1)
                        .sex('m')
                        .name("java")
                        .build();
        System.out.println(JSON.toJSONString(student));
    }
}
