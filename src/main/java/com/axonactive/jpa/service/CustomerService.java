package com.axonactive.jpa.service;

import com.axonactive.jpa.controller.request.CustomerRequest;
import com.axonactive.jpa.entities.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomer();
    Customer getCustomerById(int customerId);
    Customer addCustomer(CustomerRequest customerRequest);
    void deleteCustomer(int customerId);
    Customer updateCustomer(int customerId,CustomerRequest customerRequest);
}
