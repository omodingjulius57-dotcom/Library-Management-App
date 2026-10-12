package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private TextView tvForgotPassword;
    private MaterialButton btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> performLogin());
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void performLogin() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required");
            return;
        }

        if (dbHelper.authenticateUser(username, password)) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials! Try admin / admin", Toast.LENGTH_LONG).show();
        }
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        // Custom View for Forgot Password Dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_password, null);
        TextInputEditText etForgotUsername = dialogView.findViewById(R.id.etForgotUsername);
        TextInputEditText etForgotNewPass = dialogView.findViewById(R.id.etForgotNewPass);
        TextInputEditText etForgotConfirmPass = dialogView.findViewById(R.id.etForgotConfirmPass);

        // Pre-fill username if available
        if (etUsername.getText() != null) {
            etForgotUsername.setText(etUsername.getText().toString().trim());
        }

        builder.setView(dialogView);
        builder.setPositiveButton("RESET PASSWORD", (dialog, which) -> {
            String user = etForgotUsername.getText() != null ? etForgotUsername.getText().toString().trim() : "";
            String newPass = etForgotNewPass.getText() != null ? etForgotNewPass.getText().toString().trim() : "";
            String confirmPass = etForgotConfirmPass.getText() != null ? etForgotConfirmPass.getText().toString().trim() : "";

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(newPass)) {
                Toast.makeText(this, "Username and New Password are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = dbHelper.updatePassword(user, newPass);
            if (updated) {
                Toast.makeText(this, "Password reset successfully! Please login with your new password.", Toast.LENGTH_LONG).show();
                etPassword.setText(newPass);
            } else {
                Toast.makeText(this, "Username not found or reset failed!", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }
}
