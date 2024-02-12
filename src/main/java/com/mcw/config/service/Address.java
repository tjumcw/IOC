package com.mcw.config.service;

public class Address {
    private String street;
    private String postCode;

    public Address(String street, String postCode) {
        this.street = street;
        this.postCode = postCode;
    }

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void printStreet() {
        System.out.println("Address street: " + street);
    }

    public void printPostCode() {
        System.out.println("Address postcode: " + postCode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", postCode='" + postCode + '\'' +
                '}';
    }
}
