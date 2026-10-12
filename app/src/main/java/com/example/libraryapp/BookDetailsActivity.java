package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Book;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class BookDetailsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int bookId;
    private Book currentBook;

    private TextView tvTitle, tvAuthor, tvStatus, tvIsbn, tvCategory, tvPublisher, tvYear, tvCopies;
    private MaterialButton btnBorrow, btnEdit, btnDelete;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        dbHelper = new DatabaseHelper(this);
        bookId = getIntent().getIntExtra("BOOK_ID", -1);

        toolbar = findViewById(R.id.toolbarBookDetails);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvAuthor = findViewById(R.id.tvDetailAuthor);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvIsbn = findViewById(R.id.tvDetailIsbn);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvPublisher = findViewById(R.id.tvDetailPublisher);
        tvYear = findViewById(R.id.tvDetailYear);
        tvCopies = findViewById(R.id.tvDetailCopies);

        btnBorrow = findViewById(R.id.btnDetailBorrow);
        btnEdit = findViewById(R.id.btnDetailEdit);
        btnDelete = findViewById(R.id.btnDetailDelete);

        btnBorrow.setOnClickListener(v -> {
            if (currentBook != null && currentBook.isAvailable()) {
                Intent intent = new Intent(BookDetailsActivity.this, BorrowBookActivity.class);
                intent.putExtra("PRESELECT_BOOK_ID", currentBook.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Book currently unavailable.", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> {
            if (currentBook != null) {
                Intent intent = new Intent(BookDetailsActivity.this, EditBookActivity.class);
                intent.putExtra("BOOK_ID", currentBook.getId());
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookDetails();
    }

    private void loadBookDetails() {
        currentBook = dbHelper.getBookById(bookId);
        if (currentBook == null) {
            Toast.makeText(this, "Book not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(currentBook.getTitle());
        tvAuthor.setText("By " + currentBook.getAuthor());
        tvIsbn.setText("ISBN: " + currentBook.getIsbn());
        tvCategory.setText("Category: " + currentBook.getCategory());
        tvPublisher.setText("Publisher: " + currentBook.getPublisher());
        tvYear.setText("Publication Year: " + currentBook.getPublicationYear());
        tvCopies.setText("Total Copies: " + currentBook.getCopies() + " (" + currentBook.getAvailableCopies() + " Available)");

        if (currentBook.isAvailable()) {
            tvStatus.setText("Available");
            tvStatus.setBackgroundResource(R.drawable.bg_badge_available);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_available_text));
            btnBorrow.setEnabled(true);
        } else {
            tvStatus.setText("Borrowed");
            tvStatus.setBackgroundResource(R.drawable.bg_badge_borrowed);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_borrowed_text));
            btnBorrow.setEnabled(false);
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete '" + (currentBook != null ? currentBook.getTitle() : "") + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = dbHelper.deleteBook(bookId);
                    if (deleted) {
                        Toast.makeText(this, "Book deleted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to delete book!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
