package com.hanframework.mojito.test.service;

import com.hanframework.mojito.test.pojo.User;

/**
 * @author liuxin
 * 2020-07-31 22:03
 */
public class UserService {

    public User addUserAge(User user) {
        user.setAge(user.getAge() + 1);
        System.out.println("年龄新增:" + user.getAge());
        return user;
    }

    public void createUser(int age, String name) {
        System.out.println("年龄:" + age + ",姓名:" + name);
    }
}
