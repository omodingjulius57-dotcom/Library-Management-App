package com.example.libraryapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.adapters.BorrowRecordAdapter;
import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.BorrowRecord;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class BorrowingHistoryActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvHistory;
    private BorrowRecordAdapter adapter;
    private EditText etSearch;
    private MaterialToolbar toolbar;
    private List<BorrowRecord> allHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowing_history);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarBorrowingHistory);
        toolbar.setNavigationOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etSearchHistory);
        rvHistory = findViewById(R.id.rvBorrowingHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHistory(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        allHistoryList = dbHelper.getAllBorrowings();
        adapter = new BorrowRecordAdapter(this, allHistoryList);
        rvHistory.setAdapter(adapter);
    }

    private void filterHistory(String query) {
        if (adapter == null) return;
        if (query.isEmpty()) {
            adapter.updateList(allHistoryList);
            return;
        }

        List<BorrowRecord> filtered = new ArrayList<>();
        String q = query.toLowerCase().trim();
        for (BorrowRecord br : allHistoryList) {
            if (br.getBookTitle().toLowerCase().contains(q) ||
                br.getMemberName().toLowerCase().contains(q) ||
                br.getStatus().toLowerCase().contains(q)) {
                filtered.add(br);
            }
        }
        adapter.updateList(filtered);
    }
}
