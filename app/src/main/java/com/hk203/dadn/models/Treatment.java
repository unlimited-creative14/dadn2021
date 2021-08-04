package com.hk203.dadn.models;

public class Treatment {
    public int treatment_id;
    public String last_modified;

    public String getTreatmentDes(){
        if (treatment_id == 4) {
            return "Quan sát dấu hiệu phù phổi, suy tim";
        } else if (treatment_id == 1) {
            return "Chụp X quang ngực";
        } else if (treatment_id == 2) {
            return "Quan sát dấu hiệu bất thường";
        } else {
            return "Đo hematocrit";
        }
    }
}
