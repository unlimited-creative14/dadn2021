package com.hk203.dadn.ui.patient_info;

import java.util.Date;

public class Treatment {
    private int id;
    private String description;
    private Long time;

    public Treatment(int id, String description, Long time) {
        this.id = id;
        this.description = description;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDatetime() {
        return time;
    }

    public void setDatetime(Long time) {
        this.time = time;
    }
}
