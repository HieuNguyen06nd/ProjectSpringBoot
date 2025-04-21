package com.hieunguyen.service.impl;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Address;
import com.hieunguyen.model.User;
import com.hieunguyen.repository.AddressRepository;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    public Address createAddress(Long userId, Address addressData) {
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set user
        addressData.setUser(user);

        // Nếu isDefault = true, reset các địa chỉ mặc định khác
        if (addressData.isDefault()) {
            resetDefaultAddress(userId);
        }

        return addressRepository.save(addressData);
    }

    @Override
    public Address updateAddress(Long id, Address address) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại với ID: " + id));

        existingAddress.setFullName(address.getFullName());
        existingAddress.setPhone(address.getPhone());
        existingAddress.setProvince(address.getProvince());
        existingAddress.setDistrict(address.getDistrict());
        existingAddress.setWard(address.getWard());
        existingAddress.setDetail(address.getDetail());
        existingAddress.setDefault(address.isDefault());

        return addressRepository.save(existingAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại với ID: " + id));
        addressRepository.delete(existingAddress);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại với ID: " + id));
    }

    @Override
    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ mặc định cho user ID: " + userId));
    }

    private void resetDefaultAddress(Long userId) {
        // Lấy tất cả địa chỉ của user
        List<Address> addresses = addressRepository.findByUserId(userId);
        for (Address addr : addresses) {
            if (addr.isDefault()) {
                addr.setDefault(false);
                addressRepository.save(addr);
            }
        }
    }
}

