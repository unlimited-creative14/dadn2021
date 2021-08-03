package com.hk203.dadn.ui.admin_listuser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentListPatientDoctorBinding;
import com.hk203.dadn.models.AdminPatient;
import com.hk203.dadn.models.AdminUserProfile;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.ui.patientlist.PatientListAdapter;
import com.hk203.dadn.ui.patientlist.PatientListFragment;
import com.hk203.dadn.viewmodels.AdminListBindedPatientViewModel;
import com.hk203.dadn.viewmodels.AdminListUserViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListBindedPatientDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListBindedPatientDoctorFragment extends Fragment {
    private AdminUserProfile mDoctor;
    private AdminListBindedPatientViewModel viewModel;
    private FragmentListPatientDoctorBinding binding;
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
    public ListBindedPatientDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment ListBindedPatientDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListBindedPatientDoctorFragment newInstance(AdminUserProfile profile) {
        ListBindedPatientDoctorFragment fragment = new ListBindedPatientDoctorFragment();
        Bundle args = new Bundle();
        args.putSerializable("profile", profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDoctor = (AdminUserProfile) getArguments().get("profile");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListPatientDoctorBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(AdminListBindedPatientViewModel.class);
        binding.tvDoctorName.setText(mDoctor.toString());
        // Inflate the layout for this fragment
        viewModel.getPatients().observe(getViewLifecycleOwner(), list ->{
            ListBindedPatientAdapter adapter = new ListBindedPatientAdapter(getContext(), R.layout.benh_nhan, list);
            binding.listBindedPatientDoctor.setAdapter(adapter);
        });
        viewModel.loadPatientList(
                ((MainActivity)getActivity()).getAuthToken(),
                mDoctor.id,
                () -> Log.d("patList", "Load failed")
        );
        binding.fab.setOnClickListener( v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            List<AdminPatient> patients = new ArrayList<>();
            ArrayAdapter<AdminPatient> adapter = new ArrayAdapter<AdminPatient>(getContext(), R.layout.single_text_view, patients);

            AlertDialog ad = builder.setTitle("Them benh nhan quan ly")
                    .setAdapter(
                            adapter,
                            (dialog, which) -> {
                                int pat_id = patients.get(which).pat_id;

                                Map<String, Integer> body = new HashMap<>();
                                body.put("doctor_id", mDoctor.id);
                                repo.assignPatientToDoctor(
                                        ((MainActivity) getActivity()).getAuthToken(),
                                        pat_id,
                                        RequestBody.create(
                                                new JSONObject(body).toString(),
                                                MediaType.parse("application/json; charset=utf-8")
                                        ),
                                        new Callback<Map<String, String>>() {
                                            @Override
                                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                                viewModel.loadPatientList(
                                                        ((MainActivity)getActivity()).getAuthToken(),
                                                        mDoctor.id,
                                                        () -> Log.d("patList", "Load failed")
                                                );
                                                Toast.makeText(getContext(), response.body().getOrDefault("message", "Unknown Error"), Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<Map<String, String>> call, Throwable t) {

                                            }
                                        }
                                );
                            }).create();


            repo.listAllPatient(
                    ((MainActivity) getActivity()).getAuthToken(),
                    new Callback<List<AdminPatient>>() {
                        @Override
                        public void onResponse(Call<List<AdminPatient>> call, Response<List<AdminPatient>> response) {
                            patients.clear();
                            patients.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<List<AdminPatient>> call, Throwable t) {

                        }
                    });
            ad.show();
        });
        return binding.getRoot();
    }
}