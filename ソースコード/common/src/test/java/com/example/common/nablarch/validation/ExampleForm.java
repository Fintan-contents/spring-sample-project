package com.example.common.nablarch.validation;

import nablarch.core.validation.ee.Domain;

public class ExampleForm {

    @Domain("userName")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}