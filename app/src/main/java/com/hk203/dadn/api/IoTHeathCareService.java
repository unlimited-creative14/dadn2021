package com.hk203.dadn.api;

import com.hk203.dadn.models.Patient;
import com.hk203.dadn.models.PatientDetail;
import com.hk203.dadn.models.PutPatientInfoResponse;
import com.hk203.dadn.models.Treatment;
import com.hk203.dadn.models.UserLoginResponse;
import com.hk203.dadn.models.UserProfile;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IoTHeathCareService {
    // user login
    @POST("/user/login")
    Call<UserLoginResponse> userLogin(@Body RequestBody body);

    // admin login
    @POST("/admin/login")
    Call<UserLoginResponse> adminLogin(@Body RequestBody body);

    // get patients
    @GET("/users/patients")
    Call<List<Patient>> loadPatients(@Header("auth-token") String authToken);

    // get patient detail
    @GET("/users/patients/{patientId}")
    Call<PatientDetail> loadPatientDetail(
            @Header("auth-token") String authToken,
            @Path("patientId") int patientId
    );

    // put patient info
    @PUT("/users/patients/{patientId}")
    Call<PutPatientInfoResponse> putPatientInformation(
            @Header("auth-token") String authToken,
            @Path("patientId") int patientId,
            @Body RequestBody body
    );

    // get treatments
    @GET("/users/patients/{patientId}/treatments")
    Call<List<Treatment>> loadTreatments(
            @Header("auth-token") String authToken,
            @Path("patientId") int patientId
    );

    // get user profile
    @GET("/users/profile")
    Call<UserProfile> loadUserProfile(
            @Header("auth-token") String authToken
    );
}
