package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.adapters.BookAdapter;
import com.example.libraryapp.adapters.MemberAdapter;
import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Book;
import com.example.libraryapp.models.Member;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etSearch;
    private RecyclerView rvBooks, rvMembers;
    private BookAdapter bookAdapter;
    private MemberAdapter memberAdapter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarSearch);
        toolbar.setNavigationOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etGlobalSearch);
        rvBooks = findViewById(R.id.rvSearchBooks);
        rvMembers = findViewById(R.id.rvSearchMembers);

        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvMembers.setLayoutManager(new LinearLayoutManager(this));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Initial empty / full search
        performSearch("");
    }

    private void performSearch(String query) {
        List<Book> books = dbHelper.searchBooks(query);
        bookAdapter = new BookAdapter(this, books, book -> {
            Intent intent = new Intent(SearchActivity.this, BookDetailsActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            startActivity(intent);
        });
        rvBooks.setAdapter(bookAdapter);

        List<Member> members = dbHelper.searchMembers(query);
        memberAdapter = new MemberAdapter(this, members, member -> {
            Intent intent = new Intent(SearchActivity.this, MemberDetailsActivity.class);
            intent.putExtra("MEMBER_ID", member.getId());
            startActivity(intent);
        });
        rvMembers.setAdapter(memberAdapter);
    }
}
