package com.wx.somecode;

/**
 * @author xinquan.huangxq
 */
public interface StudentBuilder extends Builder<Student> {

    StudentBuilder age(int age);

    StudentBuilder name(String name);

    StudentBuilder sex(char sex);
}