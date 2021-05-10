package com.hk203.dadn.ui.updatehealthrule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class UpdateHealthRuleViewModel extends ViewModel {
    private MutableLiveData<ArrayList<HealthRule>> rules;

    public LiveData<ArrayList<HealthRule>> getRules()
    {
        if (rules == null)
        {
            rules = new MutableLiveData<>();
            loadRules();
        }
        return rules;
    }

    private void loadRules()
    {
        ArrayList<HealthRule> r = new ArrayList<>();
        r.add(new HealthRule(36.5f, 37.0f, 0.15f, 10, HealthRule.GREEN_LED, "Rule 1"));
        r.add(new HealthRule(37.5f, 38.0f, 0.25f, 20, HealthRule.YELLOW_LED, "Rule 2"));
        r.add(new HealthRule(38.5f, 39.0f, 0.35f, 30, HealthRule.RED_LED, "Rule 3"));
        rules.setValue(r);
    }
}
