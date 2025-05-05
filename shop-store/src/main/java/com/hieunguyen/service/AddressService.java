package com.hieunguyen.service;

import com.hieunguyen.dto.response.AddressResponse;
import com.hieunguyen.model.Address;
import java.util.List;

public interface AddressService {

    AddressResponse createAddress(Long userId, Address addressData);

    AddressResponse updateAddress(Long id, Address address);

    void deleteAddress(Long id);

    AddressResponse getAddressById(Long id);

    List<AddressResponse> getAddressesByUserId(Long userId);

    AddressResponse getDefaultAddress(Long userId);
}
