package com.example.hc_inter.Contact;

public class Contact {
    private String name;
    private String phoneNumber;

    // Constructor to initialize a contact
    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    //Get methods
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}