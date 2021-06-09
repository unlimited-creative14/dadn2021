package com.hk203.dadn.ui.patientlist;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentPatientListBinding;
import com.hk203.dadn.ui.patient_info.PatientInfoFragment;


public class PatientListFragment extends Fragment {
    private FragmentPatientListBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPatientListBinding.inflate(getLayoutInflater());
        setPatientSelectHandler();
        return binding.getRoot();
    }

    private void setPatientSelectHandler(){
        binding.lvPatients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        getActivity(),binding.lvPatients.getItemAtPosition(position).toString()
                        + " selected",Toast.LENGTH_SHORT
                ).show();
                NavController controller = Navigation.findNavController(
                        getActivity(), R.id.nav_host_fragment_content_main
                );
                Bundle bundle = new Bundle();
                bundle.putString("msg","ccccccc");
                controller.navigate(R.id.action_nav_patient_list_to_nav_patient_info,bundle);
            }
        });
    }
}