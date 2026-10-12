package com.example.libraryapp.models;

import java.io.Serializable;

public class Member implements Serializable {
    private int id;
    private String name;
    private String memberIdNumber;
    private String phone;
    private String email;
    private String address;
    private String registrationDate;

    public Member() {
    }

    public Member(int id, String name, String memberIdNumber, String phone, String email, String address, String registrationDate) {
        this.id = id;
        this.name = name;
        this.memberIdNumber = memberIdNumber;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.registrationDate = registrationDate;
    }

    public Member(String name, String memberIdNumber, String phone, String email, String address, String registrationDate) {
        this.name = name;
        this.memberIdNumber = memberIdNumber;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberIdNumber() {
        return memberIdNumber;
    }

    public void setMemberIdNumber(String memberIdNumber) {
        this.memberIdNumber = memberIdNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}
