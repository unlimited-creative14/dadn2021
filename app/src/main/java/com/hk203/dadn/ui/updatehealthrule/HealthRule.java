package com.hk203.dadn.ui.updatehealthrule;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Range;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.time.Period;
import java.util.ArrayList;

public class HealthRule implements Parcelable {
    static int nextId = 1;
    public static final int RED_LED = 3;
    public static final int GREEN_LED = 1;
    public static final int YELLOW_LED = 2;
    public static final int OFF_LED = 0;

    public static final int L3 = 3;
    public static final int L1 = 1;
    public static final int L2 = 2;
    public static final int GOOD = 0;

    protected HealthRule(Parcel in) {
        id = in.readInt();
        tempFrom = in.readFloat();
        tempTo = in.readFloat();
        timeFrom = in.readFloat();
        timeTo = in.readFloat();
        warningLevel = in.readInt();
        desc = in.readString();
    }

    public static final Creator<HealthRule> CREATOR = new Creator<HealthRule>() {
        @Override
        public HealthRule createFromParcel(Parcel in) {
            return new HealthRule(in);
        }

        @Override
        public HealthRule[] newArray(int size) {
            return new HealthRule[size];
        }
    };

    public int getId() {
        return id;
    }

    int id;
    int warningLevel;
    float tempFrom, tempTo;
    float timeFrom;
    public static ArrayList<String> getAllWarningLevel()
    {
        ArrayList<String> levels = new ArrayList<>();
        levels.add("Binh thuong");
        levels.add("Muc do 1");
        levels.add("Muc do 2");
        levels.add("Muc do 3");
        return levels;
    }
    public int getWarningLevel() {
        return warningLevel;
    }

    public void setLedState(int ledState) {
        this.warningLevel = ledState;
    }

    public float getTempFrom() {
        return tempFrom;
    }

    public void setTempFrom(float tempFrom) {
        this.tempFrom = tempFrom;
    }

    public float getTempTo() {
        return tempTo;
    }

    public void setTempTo(float tempTo) {
        this.tempTo = tempTo;
    }

    public float getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(float timeFrom) {
        this.timeFrom = timeFrom;
    }

    public float getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(float timeTo) {
        this.timeTo = timeTo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    float timeTo;
    String desc;


    public HealthRule(float tempFrom, float tempTo, float timeFrom, float timeTo, int ledState, String description)
    {
        id = nextId++;
        this.tempFrom = tempFrom;
        this.tempTo = tempTo;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.warningLevel = ledState;
        this.desc = description;
    }

    @Override
    public String toString() {
        return desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(tempFrom);
        dest.writeFloat(tempTo);
        dest.writeFloat(timeFrom);
        dest.writeFloat(timeTo);
        dest.writeInt(warningLevel);
        dest.writeString(desc);
    }
}
