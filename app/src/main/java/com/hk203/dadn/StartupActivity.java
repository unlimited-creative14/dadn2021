package com.hk203.dadn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hk203.dadn.databinding.ActivityLoginBinding;
import com.hk203.dadn.models.ErrorResponse;
import com.hk203.dadn.models.UserLoginResponse;
import com.hk203.dadn.viewmodels.SignInViewModel;

public class StartupActivity extends AppCompatActivity {
    private SignInViewModel viewModel;
    private String loginFlag;

    // Use login activity as startup activity
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkIfUserLoggedIn();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        binding.rbAdmin.setChecked(true);
        setContentView(binding.getRoot());

        // get view model
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        // user login response observer
        viewModel.getUserLoginResponse().observe(this, userLoginResponse -> {
                    if (userLoginResponse.getCode() == 200) {
                        Toast.makeText(
                                StartupActivity.this,
                                userLoginResponse.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                        UserRole role = loginFlag.equals("ms") ? UserRole.MedicalStaff : UserRole.Admin;
                        String email = binding.etEmail.getText().toString();
                        saveUser(viewModel.getAuthToken(), role, email);
                        intentToMainActivity(viewModel.getAuthToken(), role, email);
                    } else {
                        Toast.makeText(
                                StartupActivity.this,
                                userLoginResponse.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );


        // error response observer
        viewModel.getErrorResponse().observe(this, errorResponse ->
                Toast.makeText(
                        StartupActivity.this,
                        errorResponse.getMessage(),
                        Toast.LENGTH_SHORT
                ).show()
        );

        // button Login click event handler
        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            if (binding.rbMs.isChecked()) {
                loginFlag = "ms";
                viewModel.userLogIn(email, password);
            } else if (binding.rbAdmin.isChecked()) {
                loginFlag = "admin";
                viewModel.adminLogIn(email, password);
            }
        });
        binding.ivLogo.setOnClickListener(
                v -> {
                    binding.etEmail.setText("admin@gmail.com");
                    binding.etPassword.setText("admin@gmail.com");
                }
        );
    }

    private void intentToMainActivity(String authToken, UserRole role, String email) {
        Intent mIntent = new Intent(this, MainActivity.class);
        mIntent.putExtra("authToken", authToken);
        mIntent.putExtra("role", role);
        mIntent.putExtra("email", email);
        startActivity(mIntent);
        finish();
    }

    private void checkIfUserLoggedIn() {
        SharedPreferences shared = getSharedPreferences("dadn", Context.MODE_PRIVATE);
        String authToken = shared.getString("authToken", null);
        if (authToken != null) {
            UserRole userRole = UserRole.valueOf(shared.getString("role", null));
            String email = shared.getString("email", null);
            intentToMainActivity(authToken, userRole, email);
        }
    }

    private void saveUser(String authToken, UserRole role, String email) {
        SharedPreferences shared = getSharedPreferences("dadn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("authToken", authToken);
        editor.putString("role", role.toString());
        editor.putString("email", email);
        editor.apply();
    }
}