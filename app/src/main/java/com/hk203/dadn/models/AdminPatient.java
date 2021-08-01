package com.hk203.dadn.models;

public class AdminPatient extends Patient {
    public String email;
    public int dev_id;
    public int doctor_id;

    @Override
    public String toString() {
        return first_name + " " + last_name;
    }
}
