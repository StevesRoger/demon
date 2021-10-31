package com.demo.customer;

import com.demo.customer.domain.entity.Customer;
import com.demo.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;


@SpringBootApplication
public class CustomerApp implements CommandLineRunner {

    @Autowired
    private CustomerRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(CustomerApp.class, args);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void run(String... args) throws Exception {
        Customer customer = new Customer();
        customer.setName("bucky banner");
        customer.setPhone("85599553335");
        customer.setEmail("bucky@gmail.com");
        repository.save(customer);
    }
}
