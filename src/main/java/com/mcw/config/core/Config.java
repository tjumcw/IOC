package com.mcw.config.core;

import com.mcw.config.annotation.Bean;
import com.mcw.config.annotation.Configuration;
import com.mcw.config.service.Address;
import com.mcw.config.service.Content;
import com.mcw.config.service.Customer;
import com.mcw.config.service.Message;

@Configuration
public class Config {

    @Bean(value = "customer")
    public Customer customer() {
        return new Customer("mcw", "tjumcw@tju.edu.cn");
    }

    @Bean
    public Address address() {
        return new Address("jing hai road", "100000");
    }

    public Message message() {
        return new Message(new Content("Hi, there!"));
    }
}
