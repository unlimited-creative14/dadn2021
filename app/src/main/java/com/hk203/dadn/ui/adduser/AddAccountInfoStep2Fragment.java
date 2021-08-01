package com.hk203.dadn.ui.adduser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentAddAccountInfoStep2Binding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccountInfoStep2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccountInfoStep2Fragment extends Fragment {

    private FragmentAddAccountInfoStep2Binding binding;

    // TODO: Rename and change types of parameters
    private Account account;

    public AddAccountInfoStep2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddAccountInfoStep2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAccountInfoStep2Fragment newInstance() {
        AddAccountInfoStep2Fragment fragment = new AddAccountInfoStep2Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = (Account) getArguments().getSerializable("account");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddAccountInfoStep2Binding.inflate(getLayoutInflater());
        binding.tvPrevUsername.setText(account.getUsername());

        registerSubmitCallback();
        registerBackCallback();

        return binding.getRoot();
    }

    void registerBackCallback()
    {
        Button btnBack = binding.btnBack;
        btnBack.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            controller.navigateUp();
        });

    }

    void registerSubmitCallback()
    {
        Button btnSubmit = binding.btnSubmit;
        btnSubmit.setOnClickListener(v -> {


            GsonBuilder builder = new GsonBuilder();
            Gson gs = builder.create();
            String str = gs.toJson(account);


            // TODO: Do submit to server to create account
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        });
    }
}