package com.example.libraryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Book;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditBookActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int bookId;
    private Book currentBook;

    private TextInputEditText etTitle, etAuthor, etIsbn, etCategory, etPublisher, etYear, etCopies;
    private MaterialButton btnUpdate, btnDelete, btnCancel;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        dbHelper = new DatabaseHelper(this);
        bookId = getIntent().getIntExtra("BOOK_ID", -1);

        toolbar = findViewById(R.id.toolbarEditBook);
        toolbar.setNavigationOnClickListener(v -> finish());

        etTitle = findViewById(R.id.etEditBookTitle);
        etAuthor = findViewById(R.id.etEditBookAuthor);
        etIsbn = findViewById(R.id.etEditBookIsbn);
        etCategory = findViewById(R.id.etEditBookCategory);
        etPublisher = findViewById(R.id.etEditBookPublisher);
        etYear = findViewById(R.id.etEditBookYear);
        etCopies = findViewById(R.id.etEditBookCopies);

        btnUpdate = findViewById(R.id.btnEditBookUpdate);
        btnDelete = findViewById(R.id.btnEditBookDelete);
        btnCancel = findViewById(R.id.btnEditBookCancel);

        btnCancel.setOnClickListener(v -> finish());
        btnUpdate.setOnClickListener(v -> updateBook());
        btnDelete.setOnClickListener(v -> deleteBook());

        loadBookData();
    }

    private void loadBookData() {
        currentBook = dbHelper.getBookById(bookId);
        if (currentBook == null) {
            Toast.makeText(this, "Book not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etTitle.setText(currentBook.getTitle());
        etAuthor.setText(currentBook.getAuthor());
        etIsbn.setText(currentBook.getIsbn());
        etCategory.setText(currentBook.getCategory());
        etPublisher.setText(currentBook.getPublisher());
        etYear.setText(String.valueOf(currentBook.getPublicationYear()));
        etCopies.setText(String.valueOf(currentBook.getCopies()));
    }

    private void updateBook() {
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String author = etAuthor.getText() != null ? etAuthor.getText().toString().trim() : "";
        String isbn = etIsbn.getText() != null ? etIsbn.getText().toString().trim() : "";
        String category = etCategory.getText() != null ? etCategory.getText().toString().trim() : "";
        String publisher = etPublisher.getText() != null ? etPublisher.getText().toString().trim() : "";
        String yearStr = etYear.getText() != null ? etYear.getText().toString().trim() : "";
        String copiesStr = etCopies.getText() != null ? etCopies.getText().toString().trim() : "";

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(isbn)) {
            Toast.makeText(this, "Title, Author, and ISBN are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = TextUtils.isEmpty(yearStr) ? 2024 : Integer.parseInt(yearStr);
        int copies = TextUtils.isEmpty(copiesStr) ? 1 : Integer.parseInt(copiesStr);

        currentBook.setTitle(title);
        currentBook.setAuthor(author);
        currentBook.setIsbn(isbn);
        currentBook.setCategory(category);
        currentBook.setPublisher(publisher);
        currentBook.setPublicationYear(year);
        currentBook.setCopies(copies);
        // keep available copies proportion
        if (currentBook.getAvailableCopies() > copies) {
            currentBook.setAvailableCopies(copies);
        }

        boolean updated = dbHelper.updateBook(currentBook);
        if (updated) {
            Toast.makeText(this, "Book updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBook() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete this book?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = dbHelper.deleteBook(bookId);
                    if (deleted) {
                        Toast.makeText(this, "Book deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Delete failed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
