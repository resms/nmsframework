package com.nms.core.bean;

import java.util.Date;

/**
 * Created by sam on 16-10-30.
 */
public class Person extends JsonDomain{
    private String name;
    private int age;
    private Date birth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
