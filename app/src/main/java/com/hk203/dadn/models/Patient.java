package com.hk203.dadn.models;

import java.io.Serializable;

public class Patient{
    public int pat_id;
    public String first_name;
    public String last_name;
    public String phone;
    public int dev_id;
    /*public int status;*/

    public String getName() {
        return first_name + ' ' + last_name;
    }

    public String getStatus() {
        /*if (status == 1) {
            return "Recovery";
        } else if (status == 2) {
            return "Incubation";
        } else if (status == 3) {
            return "Febrile";
        } else {
            return "Emergency";
        }*/
        return "Emergency";
    }
}