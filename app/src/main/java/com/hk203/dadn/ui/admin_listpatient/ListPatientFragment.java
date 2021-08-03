package com.hk203.dadn.ui.admin_listpatient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentListPatientDoctorBinding;
import com.hk203.dadn.repositories.IoTHealthCareRepository;
import com.hk203.dadn.ui.admin_listuser.ListBindedPatientAdapter;
import com.hk203.dadn.viewmodels.AdminListBindedPatientViewModel;
import com.hk203.dadn.viewmodels.AdminListPatientViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPatientFragment extends Fragment {
    private FragmentListPatientDoctorBinding binding;
    private AdminListPatientViewModel viewmodel;
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();

    public ListPatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment ListPatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListPatientFragment newInstance(String param1, String param2) {
        ListPatientFragment fragment = new ListPatientFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListPatientDoctorBinding.inflate(inflater);
        viewmodel = new ViewModelProvider(this).get(AdminListPatientViewModel.class);
        binding.fab.setVisibility(View.GONE);
        binding.tvDoctorName.setVisibility(View.GONE);

        viewmodel.getPatients().observe(getViewLifecycleOwner(), patients -> {
            ListBindedPatientAdapter adapter = new ListBindedPatientAdapter(getContext(), R.layout.benh_nhan, patients);
            binding.listBindedPatientDoctor.setAdapter(adapter);
        });

        viewmodel.loadPatientList(
                ((MainActivity)getActivity()).getAuthToken(),
                () -> Log.d("patList", "Load failed")
        );
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}