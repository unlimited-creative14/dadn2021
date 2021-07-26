package com.hk203.dadn.ui.patient_info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentTreatmentHistoryBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TreatmentHistoryFragment extends Fragment implements AddTreatmentDialog.OnAddNewTreatmentListener {
    private FragmentTreatmentHistoryBinding binding;
    private List<Treatment> treatments = new ArrayList();

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTreatmentHistoryBinding.inflate(getLayoutInflater());
        getTreatment();
        TreatmentHistoryAdapter trmAdapter = new TreatmentHistoryAdapter(
                getContext(),
                R.layout.adapter_treatment_history,
                treatments
        );
        binding.lvTreatmentHistory.setAdapter(trmAdapter);
        binding.btnAdd.setOnClickListener(view-> showDialogAddTreatment());
        return binding.getRoot();
    }


    private void getTreatment(){
        treatments.add(new Treatment(1,"Treatment 1",System.currentTimeMillis()));
        treatments.add(new Treatment(1,"Treatment 2",System.currentTimeMillis()));
        treatments.add(new Treatment(1,"Treatment 3",System.currentTimeMillis()));
    }


    private void showDialogAddTreatment(){
        AddTreatmentDialog addTrmDialog = new AddTreatmentDialog();
        addTrmDialog.show(getChildFragmentManager(),"ADD_TREATMENT");
    }

    @Override
    public void onAddNewTreatment(String msg) {
        Log.d("CC",msg);
    }
}