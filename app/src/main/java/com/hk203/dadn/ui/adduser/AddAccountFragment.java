package com.hk203.dadn.ui.adduser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hk203.dadn.MainActivity;
import com.hk203.dadn.R;
import com.hk203.dadn.databinding.FragmentAddAccountBinding;
import com.hk203.dadn.databinding.FragmentAddAccountInfoStep2Binding;
import com.hk203.dadn.repositories.IoTHealthCareRepository;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccountFragment extends Fragment {
    private IoTHealthCareRepository repo = IoTHealthCareRepository.getInstance();
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
        AutoCompleteTextView textView = view.findViewById(R.id.et_userType);
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
                        newAcc.setUserType(Account.ADMIN);
                    }
                    else {
                        if (binding.etUserType.getText().toString().compareTo("User") == 0)
                        {
                            newAcc.setUserType(Account.USER);
                        }
                        else
                        {
                            // Handle wrong user type here
                            Toast.makeText(getContext(), "Sai loai tai khoan", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    newAcc.setUsername(binding.etRegEmail.getText().toString());
                    newAcc.setPassword(binding.etPassword.getText().toString());
                    newAcc.first_name = binding.etFirstname.getText().toString();
                    newAcc.last_name = binding.etLastname.getText().toString();
                    newAcc.cmnd = binding.etCmnd.getText().toString();
                    // switch to next frame
                    GsonBuilder builder = new GsonBuilder();
                    Gson gs = builder.create();
                    String str = gs.toJson(newAcc);

                    RequestBody body = RequestBody.create(str, MediaType.parse("application/json"));
                    repo.createUser(
                            ((MainActivity) getActivity()).getAuthToken(),
                            body,
                            new Callback<Map<String, String>>() {
                                @Override
                                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                    if(response.body() != null)
                                        Toast.makeText(getContext(), response.body().getOrDefault("message", "Unknown error"), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Map<String, String>> call, Throwable t) {

                                }
                            }
                    );
                }
            });
    }
}