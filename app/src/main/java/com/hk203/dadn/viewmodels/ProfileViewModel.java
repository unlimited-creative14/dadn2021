package com.hk203.dadn.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hk203.dadn.models.ErrorResponse;
import com.hk203.dadn.models.UserLoginResponse;
import com.hk203.dadn.models.UserProfile;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.utils.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();
    private final MutableLiveData<ErrorResponse> errorResponse = new MutableLiveData<>();

    public MutableLiveData<ErrorResponse> getErrorResponse(){
        return errorResponse;
    }

    public MutableLiveData<UserProfile> getUserProfile(){
        return userProfile;
    }

    public void loadUserProfile(String authToken){
        repo.loadUserProfile(authToken, new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()){
                    userProfile.postValue(response.body());
                }else{
                    errorResponse.postValue(ErrorUtil.parseErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });
    }
}
