package com.hk203.dadn.ui.patient_info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientInfoBinding;
import com.hk203.dadn.databinding.FragmentPatientListBinding;

public class PatientInfoFragment extends Fragment {
    private FragmentPatientInfoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPatientInfoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}