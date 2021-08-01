package com.hk203.dadn.models;


public class Patient{
    public int pat_id;
    public String first_name;
    public String last_name;
    public String phone;
    public int dev_id;

    public String getName() {
        return first_name + ' ' + last_name;
    }
}