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
import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Book;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BooksListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvBooks;
    private BookAdapter adapter;
    private EditText etSearch;
    private FloatingActionButton fabAddBook;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarBooksList);
        toolbar.setNavigationOnClickListener(v -> finish());

        String filterMode = getIntent().getStringExtra("FILTER_MODE");
        if ("AVAILABLE".equalsIgnoreCase(filterMode)) {
            toolbar.setTitle("Available Books");
        } else if ("BORROWED".equalsIgnoreCase(filterMode)) {
            toolbar.setTitle("Borrowed Books");
        }

        etSearch = findViewById(R.id.etSearchBooks);
        rvBooks = findViewById(R.id.rvBooks);
        fabAddBook = findViewById(R.id.fabAddBook);

        rvBooks.setLayoutManager(new LinearLayoutManager(this));

        fabAddBook.setOnClickListener(v -> startActivity(new Intent(this, AddBookActivity.class)));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        List<Book> bookList = dbHelper.getAllBooks();
        String filterMode = getIntent().getStringExtra("FILTER_MODE");

        if ("AVAILABLE".equalsIgnoreCase(filterMode)) {
            List<Book> availableList = new ArrayList<>();
            for (Book b : bookList) {
                if (b.isAvailable()) {
                    availableList.add(b);
                }
            }
            bookList = availableList;
        } else if ("BORROWED".equalsIgnoreCase(filterMode)) {
            List<Book> borrowedList = new ArrayList<>();
            for (Book b : bookList) {
                if (b.getAvailableCopies() < b.getCopies()) {
                    borrowedList.add(b);
                }
            }
            bookList = borrowedList;
        }

        adapter = new BookAdapter(this, bookList, book -> {
            Intent intent = new Intent(BooksListActivity.this, BookDetailsActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            startActivity(intent);
        });
        rvBooks.setAdapter(adapter);
    }

    private void filterBooks(String query) {
        if (adapter == null) return;

        List<Book> searched = dbHelper.searchBooks(query);
        String filterMode = getIntent().getStringExtra("FILTER_MODE");

        if ("AVAILABLE".equalsIgnoreCase(filterMode)) {
            List<Book> filtered = new ArrayList<>();
            for (Book b : searched) {
                if (b.isAvailable()) filtered.add(b);
            }
            adapter.updateList(filtered);
        } else if ("BORROWED".equalsIgnoreCase(filterMode)) {
            List<Book> filtered = new ArrayList<>();
            for (Book b : searched) {
                if (b.getAvailableCopies() < b.getCopies()) filtered.add(b);
            }
            adapter.updateList(filtered);
        } else {
            adapter.updateList(searched);
        }
    }
}
