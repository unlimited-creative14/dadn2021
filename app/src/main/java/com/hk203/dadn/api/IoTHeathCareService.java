package com.hk203.dadn.api;

import com.hk203.dadn.models.AdminPatient;
import com.hk203.dadn.models.AdminUserProfile;
import com.hk203.dadn.models.MqttDevice;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.models.PatientDetail;
import com.hk203.dadn.models.PutPatientInfoResponse;
import com.hk203.dadn.models.Treatment;
import com.hk203.dadn.models.UserLoginResponse;
import com.hk203.dadn.models.UserProfile;
import com.hk203.dadn.ui.updatehealthrule.HealthRule;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    // create account
    @POST("/admin/user")
    Call<Map<String, String>> createUser(
            @Header("auth-token") String authToken,
            @Body RequestBody body
    );

    // list all users
    @GET("/admin/users")
    Call<List<AdminUserProfile>> listUser(@Header("auth-token") String authToken);
    
    // list all patients binded to a doctor
    @GET("/admin/doctor/{doctorId}/patients")
    Call<List<AdminPatient>> listBindedPatientDoctor(
            @Header("auth-token") String authToken,
            @Path("doctorId") int doctorId
    );

    // list all patients
    @GET("/admin/patients")
    Call<List<AdminPatient>> listAllPatient(
            @Header("auth-token") String authToken
    );

    // assign patient to doctor
    @PUT("/admin/patients/{patientId}")
    Call<Map<String, String>> assignPatientToDoctor(
            @Header("auth-token") String authToken,
            @Path("patientId") int patientId,
            @Body RequestBody body
    );

    // list all Qtyt
    @GET("/both/qtyt")
    Call<List<HealthRule>> listAllHealthRule(
            @Header("auth-token") String authToken
    );

    // update a Qtyt
    @PUT("/admin/qtyt")
    Call<Map<String, String>> updateHealthRule(
            @Header("auth-token") String authToken,
            @Body RequestBody body
    );

    // list all device
    @GET("/both/device")
    Call<List<MqttDevice>> listAllDevice(
            @Header("auth-token") String authToken
    );

    @POST("/admin/device")
    Call<Map<String, String>> addDevice(
            @Header("auth-token") String authToken,
            @Body RequestBody body
    );

    // list all unused devices
    @GET("/both/device/false")
    Call<List<MqttDevice>> listAllUnusedDevice(
            @Header("auth-token") String authToken
    );

    // create new patient
    @POST("/admin/patients")
    Call<Map<String, Object>> createPatient(
            @Header("auth-token") String authToken,
            @Body RequestBody body
    );

    // update patient ìnfo
    @PUT("/admin/patients/{patientId}/update")
    Call<Map<String, String>> updatePatient(
            @Header("auth-token") String authToken,
            @Path("patientId") int patientId,
            @Body RequestBody body
    );

    // update user info
    @PUT("/admin/user/{userId}/update")
    Call<Map<String, String>> updateUser(
            @Header("auth-token") String authToken,
            @Path("userId") int patientId,
            @Body RequestBody body
    );

    @DELETE("/admin/patients/{id}")
    Call<Map<String, String>> deletePatient(
            @Header("auth-token") String authToken,
            @Path("id") int patientId
    );

    @DELETE("/admin/user/{id}")
    Call<Map<String, String>> deleteUser(
            @Header("auth-token") String authToken,
            @Path("id") int id
    );
}
