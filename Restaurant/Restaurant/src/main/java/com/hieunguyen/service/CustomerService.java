package com.hieunguyen.service;

import com.hieunguyen.dto.request.CustomerRequest;
import com.hieunguyen.entity.Customers;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customers> getAllCustomers();

    Optional<Customers> getCustomerById(Long id);

    Customers createCustomer(CustomerRequest request);

    Customers updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);
}
