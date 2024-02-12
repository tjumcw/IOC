package com.mcw.config.service;

import com.mcw.config.annotation.Printable;

public class Customer {
    private String name;
    private String email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Customer() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Printable
    public void printName() {
        System.out.println("Customer name: " + name);
    }

    public void printEmail() {
        System.out.println("Customer email: " + email);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
