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
import androidx.core.content.ContextCompat;

import com.example.libraryapp.database.DatabaseHelper;
import com.example.libraryapp.models.Book;
import com.example.libraryapp.models.Member;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BorrowBookActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner spinnerMembers, spinnerBooks;
    private TextView tvAvailability;
    private TextInputEditText etBorrowDate, etReturnDate;
    private MaterialButton btnSubmit;
    private MaterialToolbar toolbar;

    private List<Member> memberList = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();

    private Member selectedMember;
    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbarBorrowBook);
        toolbar.setNavigationOnClickListener(v -> finish());

        spinnerMembers = findViewById(R.id.spinnerBorrowMember);
        spinnerBooks = findViewById(R.id.spinnerBorrowBook);
        tvAvailability = findViewById(R.id.tvBorrowAvailability);

        etBorrowDate = findViewById(R.id.etBorrowDate);
        etReturnDate = findViewById(R.id.etExpectedReturnDate);
        btnSubmit = findViewById(R.id.btnSubmitBorrow);

        setupDates();
        loadSpinners();

        btnSubmit.setOnClickListener(v -> processBorrow());
    }

    private void setupDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        etBorrowDate.setText(sdf.format(today));

        // Default 14 days loan duration
        cal.add(Calendar.DAY_OF_MONTH, 14);
        etReturnDate.setText(sdf.format(cal.getTime()));
    }

    private void loadSpinners() {
        memberList = dbHelper.getAllMembers();
        bookList = dbHelper.getAllBooks();

        // Populate Members Spinner
        List<String> memberNames = new ArrayList<>();
        for (Member m : memberList) {
            memberNames.add(m.getName() + " (" + m.getMemberIdNumber() + ")");
        }
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, memberNames);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMembers.setAdapter(memberAdapter);

        spinnerMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < memberList.size()) {
                    selectedMember = memberList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Populate Books Spinner
        List<String> bookTitles = new ArrayList<>();
        int preselectBookId = getIntent().getIntExtra("PRESELECT_BOOK_ID", -1);
        int preselectPosition = 0;

        for (int i = 0; i < bookList.size(); i++) {
            Book b = bookList.get(i);
            bookTitles.add(b.getTitle() + " - " + b.getAuthor());
            if (b.getId() == preselectBookId) {
                preselectPosition = i;
            }
        }

        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookTitles);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBooks.setAdapter(bookAdapter);

        if (preselectPosition < bookList.size()) {
            spinnerBooks.setSelection(preselectPosition);
        }

        spinnerBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < bookList.size()) {
                    selectedBook = bookList.get(position);
                    updateBookAvailability();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateBookAvailability() {
        if (selectedBook == null) return;

        if (selectedBook.getAvailableCopies() > 0) {
            tvAvailability.setText("Available (" + selectedBook.getAvailableCopies() + " copies left)");
            tvAvailability.setBackgroundResource(R.drawable.bg_badge_available);
            tvAvailability.setTextColor(ContextCompat.getColor(this, R.color.status_available_text));
            btnSubmit.setEnabled(true);
        } else {
            tvAvailability.setText("Book currently unavailable.");
            tvAvailability.setBackgroundResource(R.drawable.bg_badge_borrowed);
            tvAvailability.setTextColor(ContextCompat.getColor(this, R.color.status_borrowed_text));
            btnSubmit.setEnabled(false);
        }
    }

    private void processBorrow() {
        if (selectedMember == null) {
            Toast.makeText(this, "Please select a member!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedBook == null || selectedBook.getAvailableCopies() <= 0) {
            Toast.makeText(this, "Book currently unavailable.", Toast.LENGTH_SHORT).show();
            return;
        }

        String borrowDate = etBorrowDate.getText() != null ? etBorrowDate.getText().toString().trim() : "";
        String returnDate = etReturnDate.getText() != null ? etReturnDate.getText().toString().trim() : "";

        if (TextUtils.isEmpty(borrowDate) || TextUtils.isEmpty(returnDate)) {
            Toast.makeText(this, "Borrow and Return dates are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.borrowBook(selectedBook.getId(), selectedMember.getId(), borrowDate, returnDate);
        if (success) {
            Toast.makeText(this, "Book issued successfully to " + selectedMember.getName() + "!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to issue book!", Toast.LENGTH_SHORT).show();
        }
    }
}
