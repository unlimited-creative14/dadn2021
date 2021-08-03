package com.hk203.dadn.viewmodels;

import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.models.ErrorResponse;
import com.hk203.dadn.models.FeedInfo;
import com.hk203.dadn.models.PatientDetail;
import com.hk203.dadn.models.PutPatientInfoResponse;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.utils.ErrorUtil;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientDetailViewModel extends ViewModel {
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    private final MutableLiveData<ErrorResponse> errorResponse = new MutableLiveData<>();
    private final MutableLiveData<PatientDetail> patientDetail = new MutableLiveData<>();
    private final MutableLiveData<PutPatientInfoResponse> putPatientInfoResponse = new MutableLiveData<>();
    private final MutableLiveData<FeedInfo> feedInfo = new MutableLiveData<>();


    public MutableLiveData<ErrorResponse> getErrorResponse() {
        return errorResponse;
    }

    public MutableLiveData<PatientDetail> getPatientDetail() {
        return patientDetail;
    }

    public MutableLiveData<PutPatientInfoResponse> getPutPatientInfoResponse() {
        return putPatientInfoResponse;
    }

    public MutableLiveData<FeedInfo> getFeedInfo() {
        return feedInfo;
    }

    public void loadPatientDetail(String authToken, int patientId) {
        repo.loadPatientDetail(authToken, patientId, new Callback<PatientDetail>() {
            @Override
            public void onResponse(Call<PatientDetail> call, Response<PatientDetail> response) {
                if (response.isSuccessful()) {
                    patientDetail.postValue(response.body());
                } else {
                    errorResponse.postValue(ErrorUtil.parseErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PatientDetail> call, Throwable t) {

            }
        });
    }

    public void putPatientInfo(String authToken, int patientId, int dev_id) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("dev_id", dev_id);
        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams).toString())
        );
        repo.putPatientInfo(authToken, patientId, body, new Callback<PutPatientInfoResponse>() {
            @Override
            public void onResponse(Call<PutPatientInfoResponse> call, Response<PutPatientInfoResponse> response) {
                if (response.isSuccessful()) {
                    putPatientInfoResponse.postValue(response.body());
                } else {
                    errorResponse.postValue(ErrorUtil.parseErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PutPatientInfoResponse> call, Throwable t) {

            }
        });
    }

    public void loadDeviceFeedInfo(String authToken, int devId) {
        repo.loadDeviceFeedInfo(authToken, devId, new Callback<FeedInfo>() {
            @Override
            public void onResponse(Call<FeedInfo> call, Response<FeedInfo> response) {
                if (response.isSuccessful()) {
                    feedInfo.postValue(response.body());
                } else {
                    errorResponse.postValue(ErrorUtil.parseErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<FeedInfo> call, Throwable t) {

            }
        });
    }
}
