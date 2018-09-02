package com.wx.somecode;

import com.alibaba.fastjson.JSON;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.validation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @author xinquan.huangxq
 */
public class ValidatorMain {

    public static void main(String[] args) {
        Person person = new Person();
        person.setAge(200);
        person.setName("huanglei");

        DataBinder dataBinder = new DataBinder(person);
        dataBinder.setValidator(new PersonValidator());
        dataBinder.validate();
        BindingResult results = dataBinder.getBindingResult();
        System.out.println(JSON.toJSONString(results.getAllErrors()));
    }
}
