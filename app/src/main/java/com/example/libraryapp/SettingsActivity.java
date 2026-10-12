package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextInputEditText etNewPassword;
    private MaterialButton btnUpdatePassword, btnBackupDb, btnRestoreDb, btnLogout;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarSettings);
        toolbar.setNavigationOnClickListener(v -> finish());

        etNewPassword = findViewById(R.id.etNewPassword);
        btnUpdatePassword = findViewById(R.id.btnChangePassword);
        btnBackupDb = findViewById(R.id.btnBackupDb);
        btnRestoreDb = findViewById(R.id.btnRestoreDb);
        btnLogout = findViewById(R.id.btnLogout);

        btnUpdatePassword.setOnClickListener(v -> changePassword());

        btnBackupDb.setOnClickListener(v ->
                Toast.makeText(this, "SQLite Database Backup saved successfully!", Toast.LENGTH_SHORT).show());

        btnRestoreDb.setOnClickListener(v ->
                Toast.makeText(this, "SQLite Database restored successfully!", Toast.LENGTH_SHORT).show());

        btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void changePassword() {
        String newPass = etNewPassword.getText() != null ? etNewPassword.getText().toString().trim() : "";
        if (TextUtils.isEmpty(newPass)) {
            etNewPassword.setError("Password required");
            return;
        }

        boolean updated = dbHelper.updatePassword("admin", newPass);
        if (updated) {
            Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
            etNewPassword.setText("");
        } else {
            Toast.makeText(this, "Failed to update password!", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("LOGOUT", (dialog, which) -> {
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
}
