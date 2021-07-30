package com.hk203.dadn.models;

import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.hk203.dadn.R;

import java.util.List;

public class PatientDetail {
    public int pat_id;
    public String first_name;
    public String last_name;
    public String phone;
    public int dev_id;
    public List<Temp> tempHistory;

    private int statusColor;
    private String pendingTreatment;

    static class Temp {
        float temp_value;
        String recv_time;
    }

    public String getName() {
        return first_name + ' ' + last_name;
    }

    public String getStatus() {
        float temp = tempHistory.get(0).temp_value;
        if (temp >= 27 && temp <= 27.5) {
            statusColor = R.color.green;
            pendingTreatment = "Close monitoring";
            return "Recovery";
        } else if (temp >= 27.5 && temp <= 28.5) {
            statusColor = R.color.yellow;
            pendingTreatment = "Chest X-ray";
            return "Incubation";
        } else if (temp >= 28.5 && temp <= 29.5) {
            statusColor = R.color.orange;
            pendingTreatment = "Monitoring for warning signs";
            return "Febrile";
        } else {
            pendingTreatment = "Measure Hematocrit";
            statusColor = R.color.red;
            return "Emergency";
        }
    }

    public int getStatusColor() {
        return statusColor;
    }

    public String getPendingTreatment() {
        return pendingTreatment;
    }
}


