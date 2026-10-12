package com.example.libraryapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.adapters.BorrowRecordAdapter;
import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.BorrowRecord;
import com.example.libraryapp.models.Member;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MemberDetailsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int memberId;
    private Member currentMember;

    private TextView tvName, tvCode, tvPhone, tvEmail, tvAddress, tvRegDate;
    private RecyclerView rvMemberHistory;
    private MaterialButton btnDelete;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        dbHelper = new DatabaseHelper(this);
        memberId = getIntent().getIntExtra("MEMBER_ID", -1);

        toolbar = findViewById(R.id.toolbarMemberDetails);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvName = findViewById(R.id.tvMemberDetailName);
        tvCode = findViewById(R.id.tvMemberDetailCode);
        tvPhone = findViewById(R.id.tvMemberDetailPhone);
        tvEmail = findViewById(R.id.tvMemberDetailEmail);
        tvAddress = findViewById(R.id.tvMemberDetailAddress);
        tvRegDate = findViewById(R.id.tvMemberDetailRegDate);

        rvMemberHistory = findViewById(R.id.rvMemberHistory);
        rvMemberHistory.setLayoutManager(new LinearLayoutManager(this));

        btnDelete = findViewById(R.id.btnMemberDelete);
        btnDelete.setOnClickListener(v -> confirmDelete());

        loadMemberDetails();
    }

    private void loadMemberDetails() {
        currentMember = dbHelper.getMemberById(memberId);
        if (currentMember == null) {
            Toast.makeText(this, "Member not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvName.setText(currentMember.getName());
        tvCode.setText("ID: " + currentMember.getMemberIdNumber());
        tvPhone.setText("Phone: " + currentMember.getPhone());
        tvEmail.setText("Email: " + currentMember.getEmail());
        tvAddress.setText("Address: " + currentMember.getAddress());
        tvRegDate.setText("Registered: " + currentMember.getRegistrationDate());

        List<BorrowRecord> history = dbHelper.getMemberBorrowingHistory(memberId);
        BorrowRecordAdapter adapter = new BorrowRecordAdapter(this, history);
        rvMemberHistory.setAdapter(adapter);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Member")
                .setMessage("Are you sure you want to delete '" + (currentMember != null ? currentMember.getName() : "") + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = dbHelper.deleteMember(memberId);
                    if (deleted) {
                        Toast.makeText(this, "Member deleted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to delete member!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
