package com.hk203.dadn.ui.updatehealthrule;

import java.util.ArrayList;

public class HealthRule {
    public static final int RED_LED = 3;
    public static final int GREEN_LED = 1;
    public static final int YELLOW_LED = 2;
    public static final int OFF_LED = 0;

    public int warning_level;
    public float temp_from, temp_to;
    public float duration;
    public static ArrayList<String> getAllWarningLevel()
    {
        ArrayList<String> levels = new ArrayList<>();
        levels.add("Binh thuong");
        levels.add("Muc do 1");
        levels.add("Muc do 2");
        levels.add("Muc do 3");
        return levels;
    }

    @Override
    public String toString() {
        return getAllWarningLevel().get(warning_level);
    }
}
