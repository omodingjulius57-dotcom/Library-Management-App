package com.example.libraryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.BorrowRecord;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReturnBookActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner spinnerRecords;
    private TextView tvBookTitle, tvMemberName, tvBorrowDate, tvExpectedDate;
    private TextInputEditText etActualReturnDate;
    private MaterialButton btnSubmit;
    private MaterialToolbar toolbar;

    private List<BorrowRecord> activeRecords = new ArrayList<>();
    private BorrowRecord selectedRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarReturnBook);
        toolbar.setNavigationOnClickListener(v -> finish());

        spinnerRecords = findViewById(R.id.spinnerReturnRecord);
        tvBookTitle = findViewById(R.id.tvReturnBookTitle);
        tvMemberName = findViewById(R.id.tvReturnMemberName);
        tvBorrowDate = findViewById(R.id.tvReturnBorrowDate);
        tvExpectedDate = findViewById(R.id.tvReturnExpectedDate);

        etActualReturnDate = findViewById(R.id.etActualReturnDate);
        btnSubmit = findViewById(R.id.btnSubmitReturn);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etActualReturnDate.setText(today);

        loadActiveRecords();

        btnSubmit.setOnClickListener(v -> processReturn());
    }

    private void loadActiveRecords() {
        activeRecords = dbHelper.getActiveBorrowings();

        if (activeRecords.isEmpty()) {
            Toast.makeText(this, "No active borrowed books to return!", Toast.LENGTH_SHORT).show();
            btnSubmit.setEnabled(false);
            return;
        }

        List<String> displayList = new ArrayList<>();
        for (BorrowRecord br : activeRecords) {
            displayList.add(br.getMemberName() + " - " + br.getBookTitle());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, displayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecords.setAdapter(spinnerAdapter);

        spinnerRecords.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < activeRecords.size()) {
                    selectedRecord = activeRecords.get(position);
                    updateRecordSummary();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateRecordSummary() {
        if (selectedRecord == null) return;

        tvBookTitle.setText(selectedRecord.getBookTitle());
        tvMemberName.setText("Borrowed by: " + selectedRecord.getMemberName());
        tvBorrowDate.setText("Borrow Date: " + selectedRecord.getBorrowDate());
        tvExpectedDate.setText("Expected Return: " + selectedRecord.getExpectedReturnDate());
    }

    private void processReturn() {
        if (selectedRecord == null) {
            Toast.makeText(this, "Please select a borrowing record!", Toast.LENGTH_SHORT).show();
            return;
        }

        String returnDate = etActualReturnDate.getText() != null ? etActualReturnDate.getText().toString().trim() : "";
        if (TextUtils.isEmpty(returnDate)) {
            etActualReturnDate.setError("Return date required");
            return;
        }

        boolean success = dbHelper.returnBook(selectedRecord.getId(), returnDate);
        if (success) {
            Toast.makeText(this, "Book returned successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to return book!", Toast.LENGTH_SHORT).show();
        }
    }
}
