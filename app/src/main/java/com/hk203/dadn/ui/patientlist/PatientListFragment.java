package com.hk203.dadn.ui.patientlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientListBinding;
import com.hk203.dadn.models.Patient;
import com.hk203.dadn.viewmodels.PatientsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PatientListFragment extends Fragment {
    private FragmentPatientListBinding binding;
    private List<Patient> patients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPatientListBinding.inflate(getLayoutInflater());

        ((MainActivity)getActivity()).setToolbarTitle("Danh sách bệnh nhân");

        PatientsViewModel viewModel = new ViewModelProvider(this).get(PatientsViewModel.class);

        viewModel.getPatients().observe(getViewLifecycleOwner(), patients -> {
            this.patients = patients;
            // set up patient list view
            PatientListAdapter patientListAdapter = new PatientListAdapter(
                    getContext(),
                    R.layout.adapter_patient_list,
                    patients
            );
            binding.lvPatients.setAdapter(patientListAdapter);
        });

        viewModel.getErrorResponse().observe(getViewLifecycleOwner(), errorResponse -> {
            Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });


        setPatientSelectHandler();
        setSearchByNameHandler();

        viewModel.loadPatients(((MainActivity)getActivity()).getAuthToken());

        return binding.getRoot();
    }


    private void setPatientSelectHandler() {
        binding.lvPatients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavController controller = Navigation.findNavController(
                        getActivity(),
                        R.id.nav_host_fragment_content_main
                );
                Bundle bundle = new Bundle();
                bundle.putInt("patientId", patients.get(position).pat_id);
                controller.navigate(R.id.action_nav_patient_list_to_nav_patient_info, bundle);
            }
        });
    }



    private void setSearchByNameHandler() {
        binding.svSearchByName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Patient> patientsSearched = filterPatients(newText.toLowerCase());
                PatientListAdapter patientListSearchedAdapter = new PatientListAdapter(
                        getContext(),
                        R.layout.adapter_patient_list,
                        patientsSearched
                );
                binding.lvPatients.setAdapter(patientListSearchedAdapter);
                return true;
            }
        });
    }

    private List<Patient> filterPatients(String searchKey) {
        if (searchKey.isEmpty()) {// get all
            return patients;
        } else { // filter by search key
            List<Patient> filteredPatients = new ArrayList<>();
            for (Patient p : patients) {
                if (p.getName().toLowerCase().contains(searchKey)) {
                    filteredPatients.add(p);
                }
            }
            return filteredPatients;
        }
    }
}