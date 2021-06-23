package com.hk203.dadn.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hk203.dadn.ui.patient_info.Treatment;
import com.hk203.dadn.ui.patientlist.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MedicalStaffAPI {
    MedicalStaffAPI retrofit = new Retrofit.Builder()
            .baseUrl("https://mydadn.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
            .build()
            .create(MedicalStaffAPI.class);

    @GET("users/getAllPatients")
    Call<List<Patient>> getUserInfo();

    @POST("users/addTreatment")
    Call<Treatment> addNewTreatment(@Body Treatment newTreatment);
}
