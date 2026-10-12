package com.example.smartexpensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private EditText etRegisterConfirmPassword;

    private Button btnRegister;
    private TextView tvGoToLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // Find views
        etRegisterEmail =
                findViewById(R.id.etRegisterEmail);

        etRegisterPassword =
                findViewById(R.id.etRegisterPassword);

        etRegisterConfirmPassword =
                findViewById(R.id.etRegisterConfirmPassword);

        btnRegister =
                findViewById(R.id.btnRegister);

        tvGoToLogin =
                findViewById(R.id.tvGoToLogin);

        // Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Register button
        btnRegister.setOnClickListener(v -> registerUser());

        // Go to Login
        tvGoToLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            finish();
        });
    }

    private void registerUser() {

        String email =
                etRegisterEmail.getText()
                        .toString()
                        .trim();

        String password =
                etRegisterPassword.getText()
                        .toString()
                        .trim();

        String confirmPassword =
                etRegisterConfirmPassword.getText()
                        .toString()
                        .trim();

        // Check email
        if (email.isEmpty()) {

            etRegisterEmail.setError(
                    "Enter your email"
            );

            etRegisterEmail.requestFocus();

            return;
        }

        // Check password
        if (password.isEmpty()) {

            etRegisterPassword.setError(
                    "Enter your password"
            );

            etRegisterPassword.requestFocus();

            return;
        }

        // Check confirm password
        if (confirmPassword.isEmpty()) {

            etRegisterConfirmPassword.setError(
                    "Confirm your password"
            );

            etRegisterConfirmPassword.requestFocus();

            return;
        }

        // Check password length
        if (password.length() < 6) {

            etRegisterPassword.setError(
                    "Password must be at least 6 characters"
            );

            etRegisterPassword.requestFocus();

            return;
        }

        // Check passwords match
        if (!password.equals(confirmPassword)) {

            etRegisterConfirmPassword.setError(
                    "Passwords do not match"
            );

            etRegisterConfirmPassword.requestFocus();

            return;
        }

        // Create Firebase account
        firebaseAuth
                .createUserWithEmailAndPassword(
                        email,
                        password
                )
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                RegisterActivity.this,
                                "Registration successful",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent =
                                new Intent(
                                        RegisterActivity.this,
                                        LoginActivity.class
                                );

                        startActivity(intent);

                        finish();

                    } else {

                        String errorMessage;

                        if (task.getException() != null) {

                            errorMessage =
                                    task.getException()
                                            .getMessage();

                        } else {

                            errorMessage =
                                    "Registration failed";
                        }

                        Toast.makeText(
                                RegisterActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}