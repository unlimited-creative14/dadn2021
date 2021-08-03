package com.hk203.dadn.models;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.data.Entry;
import com.hk203.dadn.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PatientDetail {
    public int pat_id;
    public String first_name;
    public String last_name;
    public String phone;
    public int dev_id;
    public List<Temp> tempHistory;

    private int statusColor = R.color.black_55;
    private String pendingTreatment = "no data!";

    public static class Temp {
        public float temp_value;
        public String recv_time;
    }

    public String getName() {
        return first_name + ' ' + last_name;
    }

    public String getStatus() {
        if (tempHistory == null || tempHistory.size() == 0) {
            return "no data!";
        }
        float temp = tempHistory.get(0).temp_value;
        if (temp <= 27) {
            statusColor = R.color.green;
            pendingTreatment = "Close monitoring";
            return "Recovery";
        } else if (temp >= 27 && temp <= 27.5) {
            statusColor = R.color.yellow;
            pendingTreatment = "Chest X-ray";
            return "Incubation";
        } else if (temp >= 27.5 && temp <= 28) {
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


