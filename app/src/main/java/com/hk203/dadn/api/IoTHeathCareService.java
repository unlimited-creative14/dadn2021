package com.hk203.dadn.api;

import com.hk203.dadn.models.UserLoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IoTHeathCareService {
    // user login
    @POST("/user/login")
    Call<UserLoginResponse> userLogin(@Body RequestBody body);
}
