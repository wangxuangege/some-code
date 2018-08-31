package com.wx.somecode;

import java.util.Date;

/**
 * @author xinquan.huangxq
 */
public class User {

    private String name;
    private int age;
    private Date birthday;
    private String a = "2";

    public String getAAA() {
        return a;
    }

    public void setAAA(String a) {
        this.a = a;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("[User:setName " + name + "]");
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", age=" + age + ", birthday=" + birthday
                + "]";
    }
}
