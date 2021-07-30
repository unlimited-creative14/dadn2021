package com.hk203.dadn.repositories;

import com.google.gson.GsonBuilder;
import com.hk203.dadn.api.IoTHeathCareService;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.models.Treatment;
import com.hk203.dadn.models.UserLoginResponse;
import com.hk203.dadn.models.UserProfile;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
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

    public void adminLogin(RequestBody body, Callback<UserLoginResponse> callback){
        ioTHeathCareService.adminLogin(body).enqueue(callback);
    }

    public void loadPatients(String authToken, Callback<List<Patient>> callback){
        ioTHeathCareService.loadPatients(authToken).enqueue(callback);
    }

    public void loadTreatments(String authToken, int patientId ,Callback<List<Treatment>> callback){
        ioTHeathCareService.loadTreatments(authToken, patientId).enqueue(callback);
    }

    public void loadUserProfile(String authToken, Callback<UserProfile> callback){
        ioTHeathCareService.loadUserProfile(authToken).enqueue(callback);
    }
}
