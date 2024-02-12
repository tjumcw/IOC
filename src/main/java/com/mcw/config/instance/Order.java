package com.mcw.config.instance;

import com.mcw.config.annotation.Autowired;
import com.mcw.config.annotation.Component;
import com.mcw.config.service.Address;
import com.mcw.config.service.Customer;

@Component
public class Order {
    private Customer customer;
    private Address address;

    @Autowired
    public Order(Customer customer, Address address) {
        this.customer = customer;
        this.address = address;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", address=" + address +
                '}';
    }
}
