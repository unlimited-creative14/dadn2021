package com.hk203.dadn.ui.adduser;

import com.hk203.dadn.models.AdminUserProfile;

import java.io.Serializable;

public class Account implements Serializable {
    public static final int ADMIN = 1;
    public static final int USER = 0;
    public void setUserType(int userType) {
        this.role = userType;
    }

    String email;
    String password;
    public int role;
    public String first_name;
    public String last_name;
    public String cmnd;

    public String getUsername() {
        return this.email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
