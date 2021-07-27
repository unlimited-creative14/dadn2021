package com.hk203.dadn.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.models.ErrorResponse;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.utils.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientsViewModel extends ViewModel {
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    private final MutableLiveData<List<Patient>> patients = new MutableLiveData<>(new ArrayList());
    private final MutableLiveData<ErrorResponse> errorResponse = new MutableLiveData<>();

    public MutableLiveData<List<Patient>> getPatients(){
        return patients;
    }

    public MutableLiveData<ErrorResponse> getErrorResponse(){
        return errorResponse;
    }

    public void loadPatients(String authToken){
        repo.loadPatients(authToken, new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()){
                    patients.postValue(response.body());
                }else{
                    errorResponse.postValue(ErrorUtil.parseErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {

            }
        });
    }
}
