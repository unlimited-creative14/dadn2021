package com.hk203.dadn.ui.admin_listuser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hk203.dadn.R;
import com.hk203.dadn.models.AdminUserProfile;
import com.hk203.dadn.databinding.FragmentAdminUserDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminUserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminUserDetailFragment extends Fragment {
    private AdminUserProfile profile;
    private FragmentAdminUserDetailBinding binding;

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
            this.profile = (AdminUserProfile) getArguments().getSerializable("profile");
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
            Fragment f = ListBindedPatientDoctorFragment.newInstance(profile);
            getFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, f)
                    .addToBackStack(null)
                    .commit();
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}