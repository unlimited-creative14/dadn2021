package com.hk203.dadn.ui.adduser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentAddAccountBinding;
import com.hk203.dadn.databinding.FragmentAddAccountInfoStep2Binding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccountFragment extends Fragment {

    public AddAccountFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAccountFragment newInstance() {
        AddAccountFragment fragment = new AddAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private FragmentAddAccountBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        // Set list account type
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,  new String[] {"Admin", "User"});
        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.et_userType);
        textView.setAdapter(adapter);

        registerAddBtnCallback();

        return binding.getRoot();

    }
    void registerAddBtnCallback()
    {
        Account newAcc = new Account();
        binding.accountThem2.setOnClickListener(v -> {
                // TODO: validate username here
                boolean usernameValid = true;

                if (usernameValid)
                {
                    if (binding.etUserType.getText().toString().compareTo("Admin") == 0)
                    {
                        newAcc.setUserType(Account.UserType.Admin);
                    }
                    else {
                        if (binding.etUserType.getText().toString().compareTo("User") == 0)
                        {
                            newAcc.setUserType(Account.UserType.User);
                        }
                        else
                        {
                            // Handle wrong user type here
                            Toast.makeText(getContext(), "Sai loai tai khoan", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    newAcc.setUsername(binding.etUserName.getText().toString());
                    newAcc.setPassword(binding.etPassword.getText().toString());
                    // switch to next frame
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("account", newAcc);

                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main, AddAccountInfoStep2Fragment.class, bundle)
                            .setReorderingAllowed(true)
                            .addToBackStack(null) // name can be null
                            .commit();

                }
            });
    }
}