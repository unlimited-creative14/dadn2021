package com.hk203.dadn.models;

import java.io.Serializable;

public class AdminUserProfile implements Serializable {
    public int id;
    public String email;
    public String created_on;
    public String modified_on;
    public int role;
    public String first_name;
    public String last_name;
    public String cmnd;

    public String getRole(){
        if (role==0){
            return "Doctor/Nurse";
        }else{
            return "Admin";
        }
    }

    @Override
    public String toString()
    {
        return id + ": " + first_name + " " + last_name;
    }
}