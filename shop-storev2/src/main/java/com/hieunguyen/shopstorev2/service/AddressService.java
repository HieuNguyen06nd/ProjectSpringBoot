package com.hieunguyen.shopstorev2.service;

import com.hieunguyen.shopstorev2.dto.request.AddressRequest;
import com.hieunguyen.shopstorev2.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse addAddress(AddressRequest request);
    List<AddressResponse> getUserAddresses();
    AddressResponse updateAddress(Long id, AddressRequest request);
    void deleteAddress(Long id);


}