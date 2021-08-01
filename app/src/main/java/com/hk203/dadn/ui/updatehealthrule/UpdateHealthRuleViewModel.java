package com.hk203.dadn.ui.updatehealthrule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.repositories.IoTHealthCareRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateHealthRuleViewModel extends ViewModel {
    private MutableLiveData<List<HealthRule>> rules;
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();

    public LiveData<List<HealthRule>> getRules()
    {
        if (rules == null)
        {
            rules = new MutableLiveData<>();
        }
        return rules;
    }

    public void loadRules(String token)
    {
        repo.listAllHealthRule(token, new Callback<List<HealthRule>>() {
            @Override
            public void onResponse(Call<List<HealthRule>> call, Response<List<HealthRule>> response) {
                rules.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<HealthRule>> call, Throwable t) {

            }
        });
    }
}
