package com.hk203.dadn.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.models.AdminPatient;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.repositories.IoTHealthCareRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminListPatientViewModel extends ViewModel {
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    private MutableLiveData<List<AdminPatient>> patients = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<AdminPatient>> getPatients() {
        return patients;
    }

    public void loadPatientList(String token, Runnable failureCb) {
        repo.listAllPatient(token, new Callback<List<AdminPatient>>() {
            @Override
            public void onResponse(Call<List<AdminPatient>> call, Response<List<AdminPatient>> response) {
                if (response.isSuccessful()) {
                    patients.postValue(response.body());
                } else
                    failureCb.run();
            }

            @Override
            public void onFailure(Call<List<AdminPatient>> call, Throwable t) {
                failureCb.run();
            }
        });
    }
}
