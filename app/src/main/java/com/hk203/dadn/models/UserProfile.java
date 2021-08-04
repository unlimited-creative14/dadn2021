package com.hk203.dadn.models;

public class UserProfile {
    public int id;
    public String email;
    public int role;

    public String getRole(){
        if (role==0){
            return "Nhân viên y tế";
        }else{
            return "Admin";
        }
    }
}
