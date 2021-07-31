package com.hk203.dadn.models;

public class Treatment {
    public int treatment_id;
    public String last_modified;

    public String getTreatmentDes(){
        if (treatment_id == 4) {
            return "Close monitoring";
        } else if (treatment_id == 1) {
            return "Chest X-ray";
        } else if (treatment_id == 2) {
            return "Monitoring for warning signs";
        } else {
            return "Measure Hematocrit";
        }
    }
}
