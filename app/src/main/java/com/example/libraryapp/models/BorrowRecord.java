package com.example.libraryapp.models;

import java.io.Serializable;

public class BorrowRecord implements Serializable {
    private int id;
    private int bookId;
    private String bookTitle;
    private int memberId;
    private String memberName;
    private String memberPhone;
    private String borrowDate;
    private String expectedReturnDate;
    private String actualReturnDate;
    private String status; // "Borrowed", "Returned", "Overdue"

    public BorrowRecord() {
    }

    public BorrowRecord(int id, int bookId, String bookTitle, int memberId, String memberName, String memberPhone, String borrowDate, String expectedReturnDate, String actualReturnDate, String status) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(String expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public String getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(String actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
