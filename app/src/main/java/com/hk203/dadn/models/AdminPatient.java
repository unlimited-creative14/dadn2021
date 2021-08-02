package com.hk203.dadn.models;

import java.io.Serializable;

public class AdminPatient implements Serializable {
    public int pat_id;
    public String first_name;
    public String last_name;
    public String phone;
    public int dev_id;
    public String email;
    public int doctor_id;

    @Override
    public String toString() {
        return first_name + " " + last_name;
    }
}
