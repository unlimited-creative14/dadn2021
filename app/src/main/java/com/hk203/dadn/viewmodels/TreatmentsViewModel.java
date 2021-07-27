package com.hk203.dadn.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.models.ErrorResponse;
import com.hk203.dadn.models.Treatment;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.utils.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreatmentsViewModel extends ViewModel {
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    private MutableLiveData<List<Treatment>> treatments = new MutableLiveData(new ArrayList());
    private final MutableLiveData<ErrorResponse> errorResponse = new MutableLiveData<>();


    public MutableLiveData<List<Treatment>> getTreatments() {
        return treatments;
    }

    public MutableLiveData<ErrorResponse> getErrorResponse() {
        return errorResponse;
    }

    public void loadTreatments(String authToken, int patientId) {
        repo.loadTreatments(authToken, patientId, new Callback<List<Treatment>>() {
            @Override
            public void onResponse(Call<List<Treatment>> call, Response<List<Treatment>> response) {
                if (response.isSuccessful()) {
                    treatments.postValue(response.body());
                } else {
                    errorResponse.postValue(ErrorUtil.parseErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<Treatment>> call, Throwable t) {

            }
        });
    }
}
