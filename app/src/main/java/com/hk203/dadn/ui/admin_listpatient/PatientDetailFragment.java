package com.hk203.dadn.ui.admin_listpatient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientDetailBinding;
import com.hk203.dadn.models.AdminPatient;
import com.hk203.dadn.models.AdminUserProfile;
import com.hk203.dadn.models.MqttDevice;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.ui.admin_devicemanager.DeviceListAdapter;
import com.hk203.dadn.ui.admin_listuser.ListUserAdapter;

import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDetailFragment extends Fragment {
    private FragmentPatientDetailBinding binding;
    private AdminPatient patient;
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();

    public PatientDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PatientDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDetailFragment newInstance(AdminPatient patient) {
        PatientDetailFragment fragment = new PatientDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("patient", patient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patient = (AdminPatient) getArguments().get("patient");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPatientDetailBinding.inflate(inflater);
        // new
        final boolean isNew = patient == null;
        if (patient == null)
        {
            patient = new AdminPatient();
            binding.patientIdTv.setText("Thêm bệnh nhân mới");
            binding.etFeedIn.setText("Chọn thiết bị");
            binding.etFeedOut.setText(" ");
            binding.etDoctorName.setText("Chọn bác sĩ");
        }
        else{
            binding.etEmail.setText(patient.email);
            binding.etFirstname.setText(patient.first_name);
            binding.etLastname.setText(patient.last_name);
            binding.etPhonenumber.setText(patient.phone);

            binding.patientIdTv.setText("Mã số bệnh nhân: " + Integer.toString(patient.pat_id));
            // load device
            repo.listAllDevice(
                    ((MainActivity) getActivity()).getAuthToken(),
                    new Callback<List<MqttDevice>>() {
                        @Override
                        public void onResponse(Call<List<MqttDevice>> call, Response<List<MqttDevice>> response) {
                            for(MqttDevice dev : response.body())
                            {
                                if (!response.isSuccessful())
                                    Toast.makeText(getContext(), "Error while load device", Toast.LENGTH_SHORT).show();
                                if(dev.dev_id == patient.dev_id)
                                {
                                    binding.etFeedIn.setText(dev.feed_in);
                                    binding.etFeedOut.setText(dev.feed_out);
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<MqttDevice>> call, Throwable t) {

                        }
                    }
            );
            // load doctor
            repo.listUser(
                    ((MainActivity) getActivity()).getAuthToken(),
                    new Callback<List<AdminUserProfile>>() {
                        @Override
                        public void onResponse(Call<List<AdminUserProfile>> call, Response<List<AdminUserProfile>> response) {
                            if(!response.isSuccessful())
                            {
                                binding.etDoctorName.setText("Error");
                                return;
                            }
                            for(AdminUserProfile pf : response.body())
                            {
                                if (pf.id == patient.doctor_id)
                                {
                                    binding.etDoctorName.setText(pf.toString());
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AdminUserProfile>> call, Throwable t) {

                        }
                    }
            );
        }

        binding.btnChooseDevice.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            Toast.makeText(getContext(), "Đang tải...", Toast.LENGTH_SHORT).show();
            repo.listAllUnusedDevice(
                    ((MainActivity) getActivity()).getAuthToken(),
                    new Callback<List<MqttDevice>>() {
                        @Override
                        public void onResponse(Call<List<MqttDevice>> call, Response<List<MqttDevice>> response) {
                            DeviceListAdapter adapter = new DeviceListAdapter(getContext(), R.layout.device_item, response.body());
                            builder.setTitle("Chọn thiết bị")
                                    .setCancelable(true)
                                    .setAdapter(adapter, (dialog, which) -> {
                                        patient.dev_id = adapter.getItem(which).dev_id;
                                        repo.listAllDevice(
                                                ((MainActivity) getActivity()).getAuthToken(),
                                                new Callback<List<MqttDevice>>() {
                                                    @Override
                                                    public void onResponse(Call<List<MqttDevice>> call, Response<List<MqttDevice>> response) {
                                                        for(MqttDevice dev : response.body())
                                                        {
                                                            if(!response.isSuccessful())
                                                            {
                                                                binding.etFeedIn.setText("Error");
                                                                binding.etFeedOut.setText("Error");
                                                                Toast.makeText(getContext(), "Error while load device", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }

                                                            if(dev.dev_id == patient.dev_id)
                                                            {
                                                                binding.etFeedIn.setText(dev.feed_in);
                                                                binding.etFeedOut.setText(dev.feed_out);
                                                                return;
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<MqttDevice>> call, Throwable t) {

                                                    }
                                                }
                                        );
                                    })
                                    .create().show();
                        }

                        @Override
                        public void onFailure(Call<List<MqttDevice>> call, Throwable t) {

                        }
                    });
        });

        binding.btnChooseDoctor.setOnClickListener(v ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            Toast.makeText(getContext(), "Đang tải...", Toast.LENGTH_SHORT).show();
            repo.listUser(
                    ((MainActivity) getActivity()).getAuthToken(),
                    new Callback<List<AdminUserProfile>>() {
                        @Override
                        public void onResponse(Call<List<AdminUserProfile>> call, Response<List<AdminUserProfile>> response) {
                            ListUserAdapter adapter = new ListUserAdapter(getContext(), R.layout.bac_sy_dieu_tri, response.body());
                            builder.setTitle("Chọn bác sĩ")
                                    .setCancelable(true)
                                    .setAdapter(adapter, (dialog, which) -> {
                                        patient.doctor_id = adapter.getItem(which).id;
                                        repo.listUser(
                                                ((MainActivity) getActivity()).getAuthToken(),
                                                new Callback<List<AdminUserProfile>>() {
                                                    @Override
                                                    public void onResponse(Call<List<AdminUserProfile>> call, Response<List<AdminUserProfile>> response) {
                                                        if(!response.isSuccessful())
                                                        {
                                                            binding.etDoctorName.setText("Error");
                                                            Toast.makeText(getContext(), "Error while loading doctor", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        for(AdminUserProfile pf : response.body())
                                                        {
                                                            if (pf.id == patient.doctor_id)
                                                            {
                                                                binding.etDoctorName.setText(pf.toString());
                                                                return;
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<AdminUserProfile>> call, Throwable t) {

                                                    }
                                                }
                                        );
                                    }).create().show();
                        }

                        @Override
                        public void onFailure(Call<List<AdminUserProfile>> call, Throwable t) {

                        }
                    });
        });
        binding.btnSave.setOnClickListener(v -> {
            if(binding.etEmail.getText().length() == 0)
            {
                Toast.makeText(getContext(), "Email không được bỏ trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if(patient.dev_id == 0)
            {
                Toast.makeText(getContext(), "Chọn thiết bị", Toast.LENGTH_SHORT).show();
                return;
            }
            if(patient.doctor_id == 0)
            {
                Toast.makeText(getContext(), "Chọn bác sĩ", Toast.LENGTH_SHORT).show();
                return;
            }
            patient.email = binding.etEmail.getText().toString();
            patient.first_name = binding.etFirstname.getText().toString();
            patient.last_name = binding.etLastname.getText().toString();
            patient.phone = binding.etPhonenumber.getText().toString();
            Gson gson = new Gson();
            RequestBody body = RequestBody.create(gson.toJson(patient), MediaType.parse("application/json"));

            if(isNew)
            {
                repo.createPatient(
                        ((MainActivity) getActivity()).getAuthToken(),
                        body,
                        new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if(response.code() == 201)
                                {
                                    Toast.makeText(getContext(), "Tạo thành công", Toast.LENGTH_LONG);
                                    clearData();

                                }else
                                {
                                    Toast.makeText(getContext(), response.body().getOrDefault("message", "Unknown error").toString(), Toast.LENGTH_LONG);
                                }
                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

                            }
                        }
                );
            }
            else{
                repo.updatePatient(
                        ((MainActivity) getActivity()).getAuthToken(),
                        patient.pat_id,
                        body,
                        new Callback<Map<String, String>>() {
                            @Override
                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                if(response.code() == 201)
                                {
                                    Toast.makeText(getContext(), "Sửa thành công", Toast.LENGTH_LONG);
                                }else
                                {
                                    Toast.makeText(getContext(), response.body().getOrDefault("message", "Unknown error").toString(), Toast.LENGTH_LONG);
                                }
                            }

                            @Override
                            public void onFailure(Call<Map<String, String>> call, Throwable t) {

                            }
                        }
                        );
            }
        });

        return binding.getRoot();
    }

    void clearData()
    {
        patient = new AdminPatient();
        setField();
    }
    void setField()
    {
        binding.etEmail.setText(patient.email);
        binding.etFirstname.setText(patient.first_name);
        binding.etLastname.setText(patient.last_name);
        binding.etPhonenumber.setText(patient.phone);
    }
}