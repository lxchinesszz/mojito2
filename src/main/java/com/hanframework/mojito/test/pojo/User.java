package com.hanframework.mojito.test.pojo;

import java.io.Serializable;

/**
 * @author liuxin
 * 2020-07-25 21:32
 */
public class User implements Serializable, TestSerialize {

    private String name = "xiaoming";

    private int age = 23;

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
