package com.hk203.dadn.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentProfileBinding;
import com.hk203.dadn.models.UserProfile;
import com.hk203.dadn.viewmodels.ProfileViewModel;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        ((MainActivity)getActivity()).setToolbarTitle("My Profile");

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> binding.setUser(userProfile));

        viewModel.getErrorResponse().observe(getViewLifecycleOwner(), errorResponse -> {
            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });

        viewModel.loadUserProfile(((MainActivity)getActivity()).getAuthToken());

        binding.logOut.setOnClickListener(v -> ((MainActivity) getActivity()).processToLogout());

        return binding.getRoot();
    }
}