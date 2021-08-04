package com.hk203.dadn.ui.patient_info;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentTreatmentHistoryBinding;
import com.hk203.dadn.viewmodels.TreatmentsViewModel;

import java.util.Collections;


public class TreatmentHistoryFragment extends Fragment{
    private FragmentTreatmentHistoryBinding binding;
    private TreatmentsViewModel viewModel;
    private int patientId;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientId = (int)getArguments().get("patientId");
        ((MainActivity)getActivity()).setToolbarTitle("Treatment History");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTreatmentHistoryBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(TreatmentsViewModel.class);

        viewModel.getTreatments().observe(getViewLifecycleOwner(), treatments -> {
            Collections.reverse(treatments);
            TreatmentHistoryAdapter trmAdapter = new TreatmentHistoryAdapter(
                    getContext(),
                    R.layout.adapter_treatment_history,
                    treatments
            );
            binding.lvTreatmentHistory.setAdapter(trmAdapter);
        });

        viewModel.loadTreatments(
                ((MainActivity) getActivity()).getAuthToken(),
                patientId
        );

        viewModel.getErrorResponse().observe(getViewLifecycleOwner(), errorResponse -> {
            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }
}