package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.adapters.BorrowRecordAdapter;
import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.BorrowRecord;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvTotalBooks, tvAvailableBooks, tvBorrowedBooks, tvTotalMembers, tvViewAllHistory;
    private CardView cardBooks, cardMembers, cardBorrow, cardReturn;
    private View btnNavSearch, btnNavHistory, btnNavOverdue, btnNavReports, btnNavSettings;
    private RecyclerView rvRecentActivity;
    private BorrowRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);

        tvTotalBooks = findViewById(R.id.tvTotalBooksCount);
        tvAvailableBooks = findViewById(R.id.tvAvailableBooksCount);
        tvBorrowedBooks = findViewById(R.id.tvBorrowedBooksCount);
        tvTotalMembers = findViewById(R.id.tvTotalMembersCount);
        tvViewAllHistory = findViewById(R.id.tvViewAllHistory);

        cardBooks = findViewById(R.id.cardManageBooks);
        cardMembers = findViewById(R.id.cardManageMembers);
        cardBorrow = findViewById(R.id.cardBorrowBook);
        cardReturn = findViewById(R.id.cardReturnBook);

        btnNavSearch = findViewById(R.id.btnNavSearch);
        btnNavHistory = findViewById(R.id.btnNavHistory);
        btnNavOverdue = findViewById(R.id.btnNavOverdue);
        btnNavReports = findViewById(R.id.btnNavReports);
        btnNavSettings = findViewById(R.id.btnNavSettings);

        rvRecentActivity = findViewById(R.id.rvRecentActivity);
        rvRecentActivity.setLayoutManager(new LinearLayoutManager(this));

        // Quick Action Clicks
        cardBooks.setOnClickListener(v -> startActivity(new Intent(this, BooksListActivity.class)));
        cardMembers.setOnClickListener(v -> startActivity(new Intent(this, MembersListActivity.class)));
        cardBorrow.setOnClickListener(v -> startActivity(new Intent(this, BorrowBookActivity.class)));
        cardReturn.setOnClickListener(v -> startActivity(new Intent(this, ReturnBookActivity.class)));

        // Bottom Nav Clicks
        btnNavSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        btnNavHistory.setOnClickListener(v -> startActivity(new Intent(this, BorrowingHistoryActivity.class)));
        tvViewAllHistory.setOnClickListener(v -> startActivity(new Intent(this, BorrowingHistoryActivity.class)));
        btnNavOverdue.setOnClickListener(v -> startActivity(new Intent(this, OverdueBooksActivity.class)));
        btnNavReports.setOnClickListener(v -> startActivity(new Intent(this, ReportsActivity.class)));
        btnNavSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        int totalBooks = dbHelper.getTotalBooksCount();
        int availableBooks = dbHelper.getAvailableBooksCount();
        int borrowedBooks = dbHelper.getBorrowedBooksCount();
        int totalMembers = dbHelper.getTotalMembersCount();

        tvTotalBooks.setText(String.valueOf(totalBooks));
        tvAvailableBooks.setText(String.valueOf(availableBooks));
        tvBorrowedBooks.setText(String.valueOf(borrowedBooks));
        tvTotalMembers.setText(String.valueOf(totalMembers));

        List<BorrowRecord> recentList = dbHelper.getAllBorrowings();
        if (recentList.size() > 5) {
            recentList = recentList.subList(0, 5);
        }

        adapter = new BorrowRecordAdapter(this, recentList);
        rvRecentActivity.setAdapter(adapter);
    }
}
