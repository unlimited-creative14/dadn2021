package com.hk203.dadn.viewmodels;

import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.hk203.dadn.api.IoTHeathCareService;
import com.hk203.dadn.models.ErrorResponse;
import com.hk203.dadn.models.UserLoginResponse;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.utils.ErrorUtil;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInViewModel extends ViewModel {
    private final IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    private final MutableLiveData<UserLoginResponse> userLoginResponse = new MutableLiveData<>();
    private final MutableLiveData<ErrorResponse> errorResponse = new MutableLiveData<>();
    private String authToken;

    public MutableLiveData<UserLoginResponse> getUserLoginResponse(){
        return userLoginResponse;
    }

    public MutableLiveData<ErrorResponse> getErrorResponse(){
        return errorResponse;
    }

    public String getAuthToken(){
        return authToken;
    }

    public void userLogIn(String email, String password){
        Map<String,Object> jsonParams = new ArrayMap<>();
        jsonParams.put("email",email);
        jsonParams.put("password",password);
        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams).toString())
        );
        repo.userLogin(body, new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.isSuccessful()){
                    userLoginResponse.postValue(response.body());
                    authToken = response.headers().get("auth-token");
                }else{
                    errorResponse.postValue(ErrorUtil.parseErrorBody(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                Log.d("CC","Log in failed -- ");
            }
        });
    }
}
