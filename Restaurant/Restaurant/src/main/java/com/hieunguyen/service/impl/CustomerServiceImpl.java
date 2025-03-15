package com.hieunguyen.service.impl;

import com.hieunguyen.dto.request.CustomerRequest;
import com.hieunguyen.entity.Customers;
import com.hieunguyen.repository.CustomersRepository;
import com.hieunguyen.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomersRepository customersRepository;

    @Override
    public List<Customers> getAllCustomers() {
        return customersRepository.findAll();
    }

    @Override
    public Optional<Customers> getCustomerById(Long id) {
        return customersRepository.findById(id);
    }

    @Override
    public Customers createCustomer(CustomerRequest request) {
        // Nếu registrationDate chưa được cung cấp, set mặc định là ngày hiện tại
        if (request.getRegistrationDate() == null) {
            request.setRegistrationDate(new Date());
        }
        Customers customer = new Customers();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        return customersRepository.save(customer);
    }

    @Override
    public Customers updateCustomer(Long id, CustomerRequest customer) {
        Customers existing = customersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setPhone(customer.getPhone());
        existing.setTotal_spent(customer.getTotal_spent());

        return customersRepository.save(existing);
    }

    @Override
    public void deleteCustomer(Long id) {
        customersRepository.deleteById(id);
    }
}
