package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.libraryapp.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;

public class ReportsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvTotal, tvAvailable, tvBorrowed, tvOverdue, tvUtilizationText;
    private CardView cardTotal, cardAvailable, cardBorrowed, cardOverdue;
    private ProgressBar progressUtilization;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarReports);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvTotal = findViewById(R.id.tvReportTotalBooks);
        tvAvailable = findViewById(R.id.tvReportAvailableBooks);
        tvBorrowed = findViewById(R.id.tvReportBorrowedBooks);
        tvOverdue = findViewById(R.id.tvReportOverdueBooks);

        cardTotal = findViewById(R.id.cardReportTotalBooks);
        cardAvailable = findViewById(R.id.cardReportAvailableBooks);
        cardBorrowed = findViewById(R.id.cardReportBorrowedBooks);
        cardOverdue = findViewById(R.id.cardReportOverdueBooks);

        tvUtilizationText = findViewById(R.id.tvReportUtilizationText);
        progressUtilization = findViewById(R.id.progressReportUtilization);

        // Click actions for stats cards
        cardTotal.setOnClickListener(v -> {
            Intent intent = new Intent(this, BooksListActivity.class);
            startActivity(intent);
        });

        cardAvailable.setOnClickListener(v -> {
            Intent intent = new Intent(this, BooksListActivity.class);
            intent.putExtra("FILTER_MODE", "AVAILABLE");
            startActivity(intent);
        });

        cardBorrowed.setOnClickListener(v -> {
            Intent intent = new Intent(this, BooksListActivity.class);
            intent.putExtra("FILTER_MODE", "BORROWED");
            startActivity(intent);
        });

        cardOverdue.setOnClickListener(v -> {
            Intent intent = new Intent(this, OverdueBooksActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReportData();
    }

    private void loadReportData() {
        int total = dbHelper.getTotalBooksCount();
        int available = dbHelper.getAvailableBooksCount();
        int borrowed = dbHelper.getBorrowedBooksCount();
        int overdue = dbHelper.getOverdueBorrowings().size();

        tvTotal.setText(String.valueOf(total));
        tvAvailable.setText(String.valueOf(available));
        tvBorrowed.setText(String.valueOf(borrowed));
        tvOverdue.setText(String.valueOf(overdue));

        int percent = total > 0 ? (available * 100) / total : 0;
        progressUtilization.setProgress(percent);
        tvUtilizationText.setText(percent + "% of total collection is available on shelves");
    }
}
