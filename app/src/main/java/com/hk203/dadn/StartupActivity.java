package com.hk203.dadn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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
                     intentToMainActivity(viewModel.getAuthToken(),role);
                 }
                 else {
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
            if (binding.rbMs.isChecked()){
                loginFlag = "ms";
                viewModel.userLogIn(email,password);
            }else if (binding.rbAdmin.isChecked()){
                loginFlag = "admin";
                viewModel.adminLogIn(email,password);
            }
        });
    }

    private void intentToMainActivity(String authToken, UserRole role){
        Intent mIntent = new Intent(this,MainActivity.class);
        mIntent.putExtra("authToken",authToken);
        mIntent.putExtra("role",role);
        startActivity(mIntent);
    }
}