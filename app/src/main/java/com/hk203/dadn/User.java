package com.hk203.dadn;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername(){
        return this.username;
    }

    public UserRole getRole(){
        return this.role;
    }
}
