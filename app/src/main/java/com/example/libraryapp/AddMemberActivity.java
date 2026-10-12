package com.example.libraryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Member;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddMemberActivity extends AppCompatActivity {

    private TextInputEditText etName, etCode, etPhone, etEmail, etAddress, etRegDate;
    private MaterialButton btnSubmit, btnCancel;
    private MaterialToolbar toolbar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarAddMember);
        toolbar.setNavigationOnClickListener(v -> finish());

        etName = findViewById(R.id.etMemberName);
        etCode = findViewById(R.id.etMemberCode);
        etPhone = findViewById(R.id.etMemberPhone);
        etEmail = findViewById(R.id.etMemberEmail);
        etAddress = findViewById(R.id.etMemberAddress);
        etRegDate = findViewById(R.id.etMemberRegDate);

        btnSubmit = findViewById(R.id.btnAddMemberSubmit);
        btnCancel = findViewById(R.id.btnAddMemberCancel);

        // Pre-fill today date
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etRegDate.setText(today);

        btnCancel.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> saveMember());
    }

    private void saveMember() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String code = etCode.getText() != null ? etCode.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String address = etAddress.getText() != null ? etAddress.getText().toString().trim() : "";
        String regDate = etRegDate.getText() != null ? etRegDate.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            etName.setError("Full name required");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number required");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            code = "LIB-" + (System.currentTimeMillis() % 10000);
        }

        Member member = new Member(name, code, phone, email, address, regDate);
        boolean success = dbHelper.addMember(member);

        if (success) {
            Toast.makeText(this, "Member registered successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
