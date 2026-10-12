package com.example.libraryapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.adapters.BorrowRecordAdapter;
import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.BorrowRecord;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class OverdueBooksActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvOverdue;
    private TextView tvHeaderCount;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue_books);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarOverdueBooks);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvHeaderCount = findViewById(R.id.tvOverdueCountHeader);
        rvOverdue = findViewById(R.id.rvOverdueBooks);
        rvOverdue.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOverdueList();
    }

    private void loadOverdueList() {
        List<BorrowRecord> overdueList = dbHelper.getOverdueBorrowings();
        tvHeaderCount.setText(overdueList.size() + " Overdue Book Record" + (overdueList.size() == 1 ? "" : "s"));

        BorrowRecordAdapter adapter = new BorrowRecordAdapter(this, overdueList);
        rvOverdue.setAdapter(adapter);
    }
}
