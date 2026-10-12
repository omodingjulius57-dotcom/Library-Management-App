package com.example.libraryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Book;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddBookActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etAuthor, etIsbn, etCategory, etPublisher, etYear, etCopies;
    private MaterialButton btnSubmit, btnCancel;
    private MaterialToolbar toolbar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarAddBook);
        toolbar.setNavigationOnClickListener(v -> finish());

        etTitle = findViewById(R.id.etAddBookTitle);
        etAuthor = findViewById(R.id.etAddBookAuthor);
        etIsbn = findViewById(R.id.etAddBookIsbn);
        etCategory = findViewById(R.id.etAddBookCategory);
        etPublisher = findViewById(R.id.etAddBookPublisher);
        etYear = findViewById(R.id.etAddBookYear);
        etCopies = findViewById(R.id.etAddBookCopies);

        btnSubmit = findViewById(R.id.btnAddBookSubmit);
        btnCancel = findViewById(R.id.btnAddBookCancel);

        btnCancel.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String author = etAuthor.getText() != null ? etAuthor.getText().toString().trim() : "";
        String isbn = etIsbn.getText() != null ? etIsbn.getText().toString().trim() : "";
        String category = etCategory.getText() != null ? etCategory.getText().toString().trim() : "";
        String publisher = etPublisher.getText() != null ? etPublisher.getText().toString().trim() : "";
        String yearStr = etYear.getText() != null ? etYear.getText().toString().trim() : "";
        String copiesStr = etCopies.getText() != null ? etCopies.getText().toString().trim() : "";

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Title required");
            return;
        }
        if (TextUtils.isEmpty(author)) {
            etAuthor.setError("Author required");
            return;
        }
        if (TextUtils.isEmpty(isbn)) {
            etIsbn.setError("ISBN required");
            return;
        }

        int year = TextUtils.isEmpty(yearStr) ? 2024 : Integer.parseInt(yearStr);
        int copies = TextUtils.isEmpty(copiesStr) ? 1 : Integer.parseInt(copiesStr);

        Book book = new Book(title, author, isbn, category, publisher, year, copies, copies);
        boolean success = dbHelper.addBook(book);

        if (success) {
            Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add book!", Toast.LENGTH_SHORT).show();
        }
    }
}
