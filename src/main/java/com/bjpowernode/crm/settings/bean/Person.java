package com.bjpowernode.crm.settings.bean;

import lombok.Data;

//ctrl+alt+l
@Data
public class Person {

    private String id;
    private String username;
    private String password;


    public static void main(String[] args) {
        Person person = new Person();
    }
}
