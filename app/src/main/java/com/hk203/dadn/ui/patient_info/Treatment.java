package com.hk203.dadn.ui.patient_info;

import java.util.Date;

public class Treatment {
    private int id;
    private String description;
    private Date datetime;

    public Treatment(int id, String description, Date datetime) {
        this.id = id;
        this.description = description;
        this.datetime = datetime;
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

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
