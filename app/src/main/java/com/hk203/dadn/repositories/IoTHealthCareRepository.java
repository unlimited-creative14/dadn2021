package com.hk203.dadn.repositories;

import com.google.gson.GsonBuilder;
import com.hk203.dadn.api.IoTHeathCareService;
import com.hk203.dadn.models.UserLoginResponse;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IoTHealthCareRepository {
    private static IoTHealthCareRepository instance;
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    private static final IoTHeathCareService ioTHeathCareService = new Retrofit.Builder()
            .baseUrl("https://mydadn.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
            .client(okHttpClient)
            .build()
            .create(IoTHeathCareService.class);

    public static IoTHealthCareRepository getInstance(){
        if (instance==null){
            instance = new IoTHealthCareRepository();
        }
        return instance;
    }

    public void userLogin(RequestBody body, Callback<UserLoginResponse> callback){
        ioTHeathCareService.userLogin(body).enqueue(callback);
    }
}
