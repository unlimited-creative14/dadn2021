package com.hk203.dadn.repositories;

import com.google.gson.GsonBuilder;
import com.hk203.dadn.api.IoTHeathCareService;
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

    public static IoTHealthCareRepository getInstance() {
        if (instance == null) {
            instance = new IoTHealthCareRepository();
        }
        return instance;
    }

    public void userLogin(RequestBody body, Callback<UserLoginResponse> callback) {
        ioTHeathCareService.userLogin(body).enqueue(callback);
    }

    public void adminLogin(RequestBody body, Callback<UserLoginResponse> callback) {
        ioTHeathCareService.adminLogin(body).enqueue(callback);
    }

    public void loadPatients(String authToken, Callback<List<Patient>> callback) {
        ioTHeathCareService.loadPatients(authToken).enqueue(callback);
    }

    public void loadPatientDetail(String authToken, int patientId, Callback<PatientDetail> callback) {
        ioTHeathCareService.loadPatientDetail(authToken, patientId).enqueue(callback);
    }

    public void putPatientInfo(
            String authToken,
            int patientId,
            RequestBody body,
            Callback<PutPatientInfoResponse> callback
    ) {
        ioTHeathCareService.putPatientInformation(authToken, patientId, body).enqueue(callback);
    }

    public void loadTreatments(String authToken, int patientId, Callback<List<Treatment>> callback) {
        ioTHeathCareService.loadTreatments(authToken, patientId).enqueue(callback);
    }

    public void loadUserProfile(String authToken, Callback<UserProfile> callback) {
        ioTHeathCareService.loadUserProfile(authToken).enqueue(callback);
    }

    public void createUser(String authToken, RequestBody body, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.createUser(authToken, body).enqueue(cb);
    }

    public void listUser(String authToken, Callback<List<AdminUserProfile>> cb)
    {
        ioTHeathCareService.listUser(authToken).enqueue(cb);
    }

    public void listBindedPatient(String authToken, int doctorId, Callback<List<AdminPatient>> cb)
    {
        ioTHeathCareService.listBindedPatientDoctor(authToken, doctorId).enqueue(cb);
    }

    public void listAllPatient(String token, Callback<List<AdminPatient>> cb)
    {
        ioTHeathCareService.listAllPatient(token).enqueue(cb);
    }

    public void assignPatientToDoctor(String token, int patientId, RequestBody body, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.assignPatientToDoctor(token, patientId, body).enqueue(cb);
    }

    public void listAllHealthRule(String token, Callback<List<HealthRule>> cb)
    {
        ioTHeathCareService.listAllHealthRule(token).enqueue(cb);
    }

    public void updateHealthRule(String token, RequestBody body, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.updateHealthRule(token, body).enqueue(cb);
    }

    public void listAllDevice(String token, Callback<List<MqttDevice>> cb)
    {
        ioTHeathCareService.listAllDevice(token).enqueue(cb);
    }

    public void createDevice(String token, RequestBody body, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.addDevice(token, body).enqueue(cb);
    }

    public void listAllUnusedDevice(String token, Callback<List<MqttDevice>> cb)
    {
        ioTHeathCareService.listAllUnusedDevice(token).enqueue(cb);
    }

    public void createPatient(String token, RequestBody body, Callback<Map<String, Object>> cb)
    {
        ioTHeathCareService.createPatient(token, body).enqueue(cb);
    }

    public void updatePatient(String token, int id, RequestBody body, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.updatePatient(token, id, body).enqueue(cb);
    }

    public void updateUser(String token, int id, RequestBody body, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.updateUser(token, id, body).enqueue(cb);
    }

    public void deleteUser(String token, int id, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.deleteUser(token, id).enqueue(cb);
    }

    public void deletePatient(String token, int id, Callback<Map<String, String>> cb)
    {
        ioTHeathCareService.deletePatient(token, id).enqueue(cb);
    }
}
