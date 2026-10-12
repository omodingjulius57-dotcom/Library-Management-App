package com.example.libraryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.libraryapp.models.Book;
import com.example.libraryapp.models.BorrowRecord;
import com.example.libraryapp.models.Member;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "library_system.db";
    private static final int DATABASE_VERSION = 3;

    // USERS Table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_ROLE = "role";

    // BOOKS Table
    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_BOOK_ID = "id";
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final String COLUMN_BOOK_AUTHOR = "author";
    public static final String COLUMN_BOOK_ISBN = "isbn";
    public static final String COLUMN_BOOK_CATEGORY = "category";
    public static final String COLUMN_BOOK_PUBLISHER = "publisher";
    public static final String COLUMN_BOOK_YEAR = "publication_year";
    public static final String COLUMN_BOOK_COPIES = "copies";
    public static final String COLUMN_BOOK_AVAILABLE = "available_copies";

    // MEMBERS Table
    public static final String TABLE_MEMBERS = "members";
    public static final String COLUMN_MEMBER_ID = "id";
    public static final String COLUMN_MEMBER_NAME = "name";
    public static final String COLUMN_MEMBER_CODE = "member_id_number";
    public static final String COLUMN_MEMBER_PHONE = "phone";
    public static final String COLUMN_MEMBER_EMAIL = "email";
    public static final String COLUMN_MEMBER_ADDRESS = "address";
    public static final String COLUMN_MEMBER_REG_DATE = "registration_date";

    // BORROWINGS Table
    public static final String TABLE_BORROWINGS = "borrowings";
    public static final String COLUMN_BORROW_ID = "id";
    public static final String COLUMN_BORROW_BOOK_ID = "book_id";
    public static final String COLUMN_BORROW_MEMBER_ID = "member_id";
    public static final String COLUMN_BORROW_DATE = "borrow_date";
    public static final String COLUMN_EXPECTED_RETURN = "expected_return_date";
    public static final String COLUMN_ACTUAL_RETURN = "actual_return_date";
    public static final String COLUMN_BORROW_STATUS = "status"; // Borrowed, Returned, Overdue

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT, " +
                COLUMN_USER_ROLE + " TEXT)";
        db.execSQL(CREATE_USERS);

        // Create Books Table
        String CREATE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOK_TITLE + " TEXT, " +
                COLUMN_BOOK_AUTHOR + " TEXT, " +
                COLUMN_BOOK_ISBN + " TEXT, " +
                COLUMN_BOOK_CATEGORY + " TEXT, " +
                COLUMN_BOOK_PUBLISHER + " TEXT, " +
                COLUMN_BOOK_YEAR + " INTEGER, " +
                COLUMN_BOOK_COPIES + " INTEGER, " +
                COLUMN_BOOK_AVAILABLE + " INTEGER)";
        db.execSQL(CREATE_BOOKS);

        // Create Members Table
        String CREATE_MEMBERS = "CREATE TABLE " + TABLE_MEMBERS + " (" +
                COLUMN_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MEMBER_NAME + " TEXT, " +
                COLUMN_MEMBER_CODE + " TEXT, " +
                COLUMN_MEMBER_PHONE + " TEXT, " +
                COLUMN_MEMBER_EMAIL + " TEXT, " +
                COLUMN_MEMBER_ADDRESS + " TEXT, " +
                COLUMN_MEMBER_REG_DATE + " TEXT)";
        db.execSQL(CREATE_MEMBERS);

        // Create Borrowings Table
        String CREATE_BORROWINGS = "CREATE TABLE " + TABLE_BORROWINGS + " (" +
                COLUMN_BORROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BORROW_BOOK_ID + " INTEGER, " +
                COLUMN_BORROW_MEMBER_ID + " INTEGER, " +
                COLUMN_BORROW_DATE + " TEXT, " +
                COLUMN_EXPECTED_RETURN + " TEXT, " +
                COLUMN_ACTUAL_RETURN + " TEXT, " +
                COLUMN_BORROW_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_BORROW_BOOK_ID + ") REFERENCES " + TABLE_BOOKS + "(" + COLUMN_BOOK_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BORROW_MEMBER_ID + ") REFERENCES " + TABLE_MEMBERS + "(" + COLUMN_MEMBER_ID + "))";
        db.execSQL(CREATE_BORROWINGS);

        // Seed Default Admin User
        ContentValues adminValues = new ContentValues();
        adminValues.put(COLUMN_USER_NAME, "admin");
        adminValues.put(COLUMN_USER_PASSWORD, "admin");
        adminValues.put(COLUMN_USER_ROLE, "Librarian");
        db.insert(TABLE_USERS, null, adminValues);

        // Seed Sample Books, Members & Borrowings
        seedSampleData(db);
    }

    private void seedSampleData(SQLiteDatabase db) {
        // Official Books List (20 books)
        insertBookDirect(db, "Java: The Complete Reference", "Herbert Schildt", "9781260440232", "Programming", "McGraw-Hill", 2021, 5, 5);
        insertBookDirect(db, "Head First Java", "Kathy Sierra, Bert Bates", "9780596009205", "Programming", "O'Reilly Media", 2005, 4, 3);
        insertBookDirect(db, "Effective Java", "Joshua Bloch", "9780134685991", "Programming", "Addison-Wesley", 2018, 4, 2);
        insertBookDirect(db, "Clean Code", "Robert C. Martin", "9780132350884", "Software Engineering", "Prentice Hall", 2008, 5, 4);
        insertBookDirect(db, "Database System Concepts", "Abraham Silberschatz, Henry Korth, S. Sudarshan", "9780078022159", "Database", "McGraw-Hill", 2019, 5, 5);
        insertBookDirect(db, "Fundamentals of Database Systems", "Ramez Elmasri, Shamkant Navathe", "9780133970777", "Database", "Pearson", 2016, 4, 3);
        insertBookDirect(db, "Learning SQL", "Alan Beaulieu", "9780596520830", "Database", "O'Reilly Media", 2009, 3, 3);
        insertBookDirect(db, "Computer Networking: A Top-Down Approach", "James Kurose, Keith Ross", "9780136681557", "Networking", "Pearson", 2021, 5, 4);
        insertBookDirect(db, "Data Communications and Networking", "Behrouz A. Forouzan", "9780073376226", "Networking", "McGraw-Hill", 2012, 4, 4);
        insertBookDirect(db, "Operating System Concepts", "Abraham Silberschatz, Peter Baer Galvin, Greg Gagne", "9781119800361", "Operating Systems", "Wiley", 2021, 5, 5);
        insertBookDirect(db, "Modern Operating Systems", "Andrew S. Tanenbaum, Herbert Bos", "9780137618873", "Operating Systems", "Pearson", 2022, 4, 3);
        insertBookDirect(db, "Software Engineering", "Ian Sommerville", "9780133943030", "Software Engineering", "Pearson", 2016, 5, 4);
        insertBookDirect(db, "Introduction to Algorithms", "Thomas H. Cormen et al.", "9780262046305", "Computer Science", "MIT Press", 2022, 4, 2);
        insertBookDirect(db, "Artificial Intelligence: A Modern Approach", "Stuart Russell, Peter Norvig", "9780134610993", "Artificial Intelligence", "Pearson", 2021, 3, 3);
        insertBookDirect(db, "Python Crash Course", "Eric Matthes", "9781718502703", "Programming", "No Starch Press", 2023, 5, 5);
        insertBookDirect(db, "HTML and CSS: Design and Build Websites", "Jon Duckett", "9781118008188", "Web Development", "Wiley", 2011, 4, 4);
        insertBookDirect(db, "JavaScript: The Definitive Guide", "David Flanagan", "9781491952023", "Web Development", "O'Reilly Media", 2020, 3, 2);
        insertBookDirect(db, "Don't Make Me Think", "Steve Krug", "9780321965516", "UI/UX Design", "New Riders", 2014, 3, 3);
        insertBookDirect(db, "Computer Science Illuminated", "Nell Dale, John Lewis", "9781284155610", "Computer Science", "Jones & Bartlett Learning", 2019, 4, 4);
        insertBookDirect(db, "Management Information Systems", "Kenneth C. Laudon, Jane P. Laudon", "9780136971790", "Information Systems", "Pearson", 2022, 4, 3);

        // Official Members List
        insertMemberDirect(db, "Derrick Dickens Omoding", "LIB 1001", "0788988309", "ddomoding.std@nkumbauniversity.ac.ug", "Kasenyi", "2024-01-15");
        insertMemberDirect(db, "Tamale Matia", "LIB 1002", "0756230321", "tmatia.std@nkumbauniversity.ac.ug", "Bwaise", "2024-02-10");
        insertMemberDirect(db, "Namataaka Lucy Flavia", "LIB 1003", "0744251425", "flnamataaka.std@nkumbauniversity.ac.ug", "Mpala", "2024-03-01");
        insertMemberDirect(db, "Namuddu Esther", "LIB 1004", "0785226818", "enamuddu.std@nkumbauniversity.ac.ug", "Bwaise", "2024-04-12");
        insertMemberDirect(db, "Kyenune Moses", "LIB 1005", "0709852330", "kmoses.std@nkumbauniversity.ac.ug", "Katabi", "2024-05-05");

        // Sample Borrowings
        insertBorrowDirect(db, 2, 1, "2025-06-01", "2025-06-15", null, "Borrowed");
        insertBorrowDirect(db, 3, 2, "2025-05-10", "2025-05-24", "2025-05-22", "Returned");
        insertBorrowDirect(db, 13, 3, "2025-05-01", "2025-05-15", null, "Overdue");
    }

    private void insertBookDirect(SQLiteDatabase db, String title, String author, String isbn, String cat, String pub, int year, int copies, int available) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_TITLE, title);
        cv.put(COLUMN_BOOK_AUTHOR, author);
        cv.put(COLUMN_BOOK_ISBN, isbn);
        cv.put(COLUMN_BOOK_CATEGORY, cat);
        cv.put(COLUMN_BOOK_PUBLISHER, pub);
        cv.put(COLUMN_BOOK_YEAR, year);
        cv.put(COLUMN_BOOK_COPIES, copies);
        cv.put(COLUMN_BOOK_AVAILABLE, available);
        db.insert(TABLE_BOOKS, null, cv);
    }

    private void insertMemberDirect(SQLiteDatabase db, String name, String code, String phone, String email, String address, String regDate) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEMBER_NAME, name);
        cv.put(COLUMN_MEMBER_CODE, code);
        cv.put(COLUMN_MEMBER_PHONE, phone);
        cv.put(COLUMN_MEMBER_EMAIL, email);
        cv.put(COLUMN_MEMBER_ADDRESS, address);
        cv.put(COLUMN_MEMBER_REG_DATE, regDate);
        db.insert(TABLE_MEMBERS, null, cv);
    }

    private void insertBorrowDirect(SQLiteDatabase db, int bookId, int memberId, String borrowDate, String expected, String actual, String status) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BORROW_BOOK_ID, bookId);
        cv.put(COLUMN_BORROW_MEMBER_ID, memberId);
        cv.put(COLUMN_BORROW_DATE, borrowDate);
        cv.put(COLUMN_EXPECTED_RETURN, expected);
        cv.put(COLUMN_ACTUAL_RETURN, actual);
        cv.put(COLUMN_BORROW_STATUS, status);
        db.insert(TABLE_BORROWINGS, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BORROWINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- AUTHENTICATION ---
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USER_NAME + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean success = cursor.getCount() > 0;
        cursor.close();
        return success;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, cv, COLUMN_USER_NAME + "=?", new String[]{username});
        return rows > 0;
    }

    // --- BOOKS CRUD ---
    public boolean addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_TITLE, book.getTitle());
        cv.put(COLUMN_BOOK_AUTHOR, book.getAuthor());
        cv.put(COLUMN_BOOK_ISBN, book.getIsbn());
        cv.put(COLUMN_BOOK_CATEGORY, book.getCategory());
        cv.put(COLUMN_BOOK_PUBLISHER, book.getPublisher());
        cv.put(COLUMN_BOOK_YEAR, book.getPublicationYear());
        cv.put(COLUMN_BOOK_COPIES, book.getCopies());
        cv.put(COLUMN_BOOK_AVAILABLE, book.getAvailableCopies());
        long result = db.insert(TABLE_BOOKS, null, cv);
        return result != -1;
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, null, null, null, null, COLUMN_BOOK_TITLE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Book b = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ISBN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_PUBLISHER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_YEAR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_COPIES)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AVAILABLE))
                );
                list.add(b);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Book getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, COLUMN_BOOK_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Book book = null;
        if (cursor.moveToFirst()) {
            book = new Book(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ISBN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_PUBLISHER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_YEAR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_COPIES)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AVAILABLE))
            );
        }
        cursor.close();
        return book;
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BOOK_TITLE, book.getTitle());
        cv.put(COLUMN_BOOK_AUTHOR, book.getAuthor());
        cv.put(COLUMN_BOOK_ISBN, book.getIsbn());
        cv.put(COLUMN_BOOK_CATEGORY, book.getCategory());
        cv.put(COLUMN_BOOK_PUBLISHER, book.getPublisher());
        cv.put(COLUMN_BOOK_YEAR, book.getPublicationYear());
        cv.put(COLUMN_BOOK_COPIES, book.getCopies());
        cv.put(COLUMN_BOOK_AVAILABLE, book.getAvailableCopies());
        int rows = db.update(TABLE_BOOKS, cv, COLUMN_BOOK_ID + "=?", new String[]{String.valueOf(book.getId())});
        return rows > 0;
    }

    public boolean deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BOOKS, COLUMN_BOOK_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public List<Book> searchBooks(String query) {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_BOOK_TITLE + " LIKE ? OR " +
                COLUMN_BOOK_AUTHOR + " LIKE ? OR " +
                COLUMN_BOOK_ISBN + " LIKE ? OR " +
                COLUMN_BOOK_CATEGORY + " LIKE ?";
        String wildcard = "%" + query + "%";
        Cursor cursor = db.query(TABLE_BOOKS, null, selection,
                new String[]{wildcard, wildcard, wildcard, wildcard}, null, null, COLUMN_BOOK_TITLE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Book b = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ISBN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_PUBLISHER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_YEAR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_COPIES)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AVAILABLE))
                );
                list.add(b);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- MEMBERS CRUD ---
    public boolean addMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEMBER_NAME, member.getName());
        cv.put(COLUMN_MEMBER_CODE, member.getMemberIdNumber());
        cv.put(COLUMN_MEMBER_PHONE, member.getPhone());
        cv.put(COLUMN_MEMBER_EMAIL, member.getEmail());
        cv.put(COLUMN_MEMBER_ADDRESS, member.getAddress());
        cv.put(COLUMN_MEMBER_REG_DATE, member.getRegistrationDate());
        long result = db.insert(TABLE_MEMBERS, null, cv);
        return result != -1;
    }

    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMBERS, null, null, null, null, null, COLUMN_MEMBER_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Member m = new Member(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_REG_DATE))
                );
                list.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Member getMemberById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMBERS, null, COLUMN_MEMBER_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Member m = null;
        if (cursor.moveToFirst()) {
            m = new Member(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_REG_DATE))
            );
        }
        cursor.close();
        return m;
    }

    public boolean updateMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEMBER_NAME, member.getName());
        cv.put(COLUMN_MEMBER_CODE, member.getMemberIdNumber());
        cv.put(COLUMN_MEMBER_PHONE, member.getPhone());
        cv.put(COLUMN_MEMBER_EMAIL, member.getEmail());
        cv.put(COLUMN_MEMBER_ADDRESS, member.getAddress());
        cv.put(COLUMN_MEMBER_REG_DATE, member.getRegistrationDate());
        int rows = db.update(TABLE_MEMBERS, cv, COLUMN_MEMBER_ID + "=?", new String[]{String.valueOf(member.getId())});
        return rows > 0;
    }

    public boolean deleteMember(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_MEMBERS, COLUMN_MEMBER_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public List<Member> searchMembers(String query) {
        List<Member> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_MEMBER_NAME + " LIKE ? OR " +
                COLUMN_MEMBER_CODE + " LIKE ? OR " +
                COLUMN_MEMBER_PHONE + " LIKE ? OR " +
                COLUMN_MEMBER_EMAIL + " LIKE ?";
        String wildcard = "%" + query + "%";
        Cursor cursor = db.query(TABLE_MEMBERS, null, selection,
                new String[]{wildcard, wildcard, wildcard, wildcard}, null, null, COLUMN_MEMBER_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Member m = new Member(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_REG_DATE))
                );
                list.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- BORROWINGS CRUD ---
    public boolean borrowBook(int bookId, int memberId, String borrowDate, String expectedReturnDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Check availability
            Book book = getBookById(bookId);
            if (book == null || book.getAvailableCopies() <= 0) {
                return false;
            }

            // Insert borrowing record
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_BORROW_BOOK_ID, bookId);
            cv.put(COLUMN_BORROW_MEMBER_ID, memberId);
            cv.put(COLUMN_BORROW_DATE, borrowDate);
            cv.put(COLUMN_EXPECTED_RETURN, expectedReturnDate);
            cv.put(COLUMN_BORROW_STATUS, "Borrowed");
            long id = db.insert(TABLE_BORROWINGS, null, cv);

            if (id == -1) return false;

            // Decrement available copies
            ContentValues bookCv = new ContentValues();
            bookCv.put(COLUMN_BOOK_AVAILABLE, book.getAvailableCopies() - 1);
            db.update(TABLE_BOOKS, bookCv, COLUMN_BOOK_ID + "=?", new String[]{String.valueOf(bookId)});

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public boolean returnBook(int borrowId, String actualReturnDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Get borrowing record
            Cursor cursor = db.query(TABLE_BORROWINGS, null, COLUMN_BORROW_ID + "=?", new String[]{String.valueOf(borrowId)}, null, null, null);
            if (!cursor.moveToFirst()) {
                cursor.close();
                return false;
            }
            int bookId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BORROW_BOOK_ID));
            cursor.close();

            // Update borrow record
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ACTUAL_RETURN, actualReturnDate);
            cv.put(COLUMN_BORROW_STATUS, "Returned");
            db.update(TABLE_BORROWINGS, cv, COLUMN_BORROW_ID + "=?", new String[]{String.valueOf(borrowId)});

            // Increment book available copies
            Book book = getBookById(bookId);
            if (book != null) {
                ContentValues bookCv = new ContentValues();
                bookCv.put(COLUMN_BOOK_AVAILABLE, book.getAvailableCopies() + 1);
                db.update(TABLE_BOOKS, bookCv, COLUMN_BOOK_ID + "=?", new String[]{String.valueOf(bookId)});
            }

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public List<BorrowRecord> getAllBorrowings() {
        List<BorrowRecord> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.id, b.book_id, bk.title as book_title, b.member_id, m.name as member_name, m.phone as member_phone, " +
                "b.borrow_date, b.expected_return_date, b.actual_return_date, b.status " +
                "FROM " + TABLE_BORROWINGS + " b " +
                "JOIN " + TABLE_BOOKS + " bk ON b.book_id = bk.id " +
                "JOIN " + TABLE_MEMBERS + " m ON b.member_id = m.id " +
                "ORDER BY b.id DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                BorrowRecord br = new BorrowRecord(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("book_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("book_title")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("member_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("member_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("member_phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("borrow_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expected_return_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("actual_return_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status"))
                );
                list.add(br);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<BorrowRecord> getActiveBorrowings() {
        List<BorrowRecord> list = new ArrayList<>();
        for (BorrowRecord record : getAllBorrowings()) {
            if ("Borrowed".equalsIgnoreCase(record.getStatus()) || "Overdue".equalsIgnoreCase(record.getStatus())) {
                list.add(record);
            }
        }
        return list;
    }

    public List<BorrowRecord> getOverdueBorrowings() {
        List<BorrowRecord> list = new ArrayList<>();
        for (BorrowRecord record : getAllBorrowings()) {
            if ("Overdue".equalsIgnoreCase(record.getStatus())) {
                list.add(record);
            }
        }
        return list;
    }

    public List<BorrowRecord> getMemberBorrowingHistory(int memberId) {
        List<BorrowRecord> list = new ArrayList<>();
        for (BorrowRecord record : getAllBorrowings()) {
            if (record.getMemberId() == memberId) {
                list.add(record);
            }
        }
        return list;
    }

    // --- STATISTICS ---
    public int getTotalBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_BOOK_COPIES + ") FROM " + TABLE_BOOKS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getAvailableBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_BOOK_AVAILABLE + ") FROM " + TABLE_BOOKS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getBorrowedBooksCount() {
        return getTotalBooksCount() - getAvailableBooksCount();
    }

    public int getTotalMembersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_MEMBERS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
