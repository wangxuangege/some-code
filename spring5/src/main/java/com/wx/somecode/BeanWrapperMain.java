package com.wx.somecode;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.lang.Nullable;

import java.beans.PropertyEditorSupport;

/**
 * @author xinquan.huangxq
 */
public class BeanWrapperMain {

    public static void main(String[] args) {
        Person person = new Person();
        person.setName("huanglei");
        person.setAge(29);

        BeanWrapper beanWrapper = new BeanWrapperImpl(person);
        System.out.println(beanWrapper.getPropertyValue("age"));
        System.out.println(beanWrapper.getPropertyType("name"));

        beanWrapper.setPropertyValue("name", "wangxuan");
        System.out.println(person.getName());

        beanWrapper.registerCustomEditor(int.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(@Nullable String text) throws IllegalArgumentException {
                setValue(Integer.parseInt(text) * 2);
            }
        });
        beanWrapper.setPropertyValue("age", "20");
        System.out.println(person.getAge());
        System.out.println(beanWrapper.getPropertyValue("age"));
    }
}
