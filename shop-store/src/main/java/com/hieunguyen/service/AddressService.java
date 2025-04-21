package com.hieunguyen.service;

import java.util.List;
import com.hieunguyen.model.Address;

public interface AddressService {
    Address createAddress(Long userId, Address addressData);
    Address updateAddress(Long id, Address address);
    void deleteAddress(Long id);
    Address getAddressById(Long id);
    List<Address> getAddressesByUserId(Long userId);
    Address getDefaultAddress(Long userId);
}

