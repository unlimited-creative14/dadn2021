package com.hk203.dadn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hk203.dadn.databinding.ActivityLoginBinding;

public class StartupActivity extends AppCompatActivity {
    // Use login activity as startup activity
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerLoginCallback();
    }

    void registerLoginCallback()
    {

        Button loginBtn = binding.btnLogin;
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                // TODO: Do some login stuff here before start main activity

                EditText et_username = binding.etUsername;
                EditText et_password = binding.etPassword;
                android.content.Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user",new User(
                        et_username.getText().toString(),et_password.getText().toString(),UserRole.MedicalStaff
                ));
                startActivity(intent);
                finish();
            }
        });
    }
}