package com.hk203.dadn.ui.patientlist;

import java.io.Serializable;

public class Patient implements Serializable {
    private String name;
    private String status;

    public Patient(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
