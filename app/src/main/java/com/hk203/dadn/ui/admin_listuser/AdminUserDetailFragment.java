package com.hk203.dadn.ui.admin_listuser;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.models.AdminUserProfile;
import com.hk203.dadn.databinding.FragmentAdminUserDetailBinding;
import com.hk203.dadn.repositories.IoTHealthCareRepository;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminUserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminUserDetailFragment extends Fragment {
    private AdminUserProfile profile;
    private FragmentAdminUserDetailBinding binding;
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();

    // TODO: Rename and change types of parameters
    public AdminUserDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment AdminUserDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminUserDetailFragment newInstance(AdminUserProfile profile) {
        AdminUserDetailFragment fragment = new AdminUserDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("profile", profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.profile = (AdminUserProfile) getArguments().get("profile");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUserDetailBinding.inflate(inflater);
        binding.etEmail.setText(profile.email);
        binding.etCreatedate.setText(profile.created_on);
        binding.etFirstname.setText(profile.first_name);
        binding.etLastname.setText(profile.last_name);
        binding.etCmnd.setText(profile.cmnd);

        binding.btnViewBindedPatients.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(
                    getActivity(),
                    R.id.nav_host_fragment_content_main
            );
            Bundle bd = new Bundle();
            bd.putSerializable("profile", profile);

            controller.navigate(R.id.nav_list_binded_patient_doctor, bd);
        });

        binding.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext()).setTitle("Xác nhận xoá user")
                    .setMessage("Xoá !?".replace("!", profile.toString()))
                    .setPositiveButton("Ok", (d, l) -> {
                        repo.deleteUser(
                                ((MainActivity) getContext()).getAuthToken(),
                                profile.id,
                                new Callback<Map<String, String>>() {
                                    @Override
                                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                        if(response.code() == 200)
                                        {
                                            Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                            getParentFragmentManager().popBackStack();
                                        }
                                        else
                                            Toast.makeText(getContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Map<String, String>> call, Throwable t) {

                                    }
                                }
                        );
                    })
                    .setNegativeButton("Huỷ", (d, l) -> d.cancel())
                    .create().show();

        });

        binding.btnSave.setOnClickListener(v -> {
            profile.first_name = binding.etFirstname.getText().toString();
            profile.last_name = binding.etLastname.getText().toString();
            profile.cmnd = binding.etCmnd.getText().toString();

            Gson gson = new Gson();
            RequestBody body = RequestBody.create(gson.toJson(profile), MediaType.parse("application/json"));
            repo.updateUser(
                    ((MainActivity) getContext()).getAuthToken(),
                    profile.id,
                    body,
                    new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.code() == 200)
                            {
                                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getContext(), "Save failed!", Toast.LENGTH_SHORT).show();
                        }
                        

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {

                        }
                    }
            );
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}