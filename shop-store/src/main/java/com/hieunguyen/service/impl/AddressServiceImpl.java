package com.hieunguyen.service.impl;

import com.hieunguyen.dto.response.AddressResponse;
import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Address;
import com.hieunguyen.model.User;
import com.hieunguyen.repository.AddressRepository;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse createAddress(Long userId, Address addressData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Set user và xử lý default
        addressData.setUser(user);
        if (addressData.isDefault()) {
            resetDefaultAddress(userId);
        }

        Address saved = addressRepository.save(addressData);
        return toDto(saved);
    }

    @Override
    public AddressResponse updateAddress(Long id, Address address) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại với ID: " + id));

        existing.setFullName(address.getFullName());
        existing.setPhone(address.getPhone());
        existing.setProvince(address.getProvince());
        existing.setDistrict(address.getDistrict());
        existing.setWard(address.getWard());
        existing.setDetail(address.getDetail());
        existing.setDefault(address.isDefault());

        Address updated = addressRepository.save(existing);
        return toDto(updated);
    }

    @Override
    public void deleteAddress(Long id) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại với ID: " + id));
        addressRepository.delete(existing);
    }

    @Override
    public AddressResponse getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại với ID: " + id));
        return toDto(address);
    }

    @Override
    public List<AddressResponse> getAddressesByUserId(Long userId) {
        List<Address> list = addressRepository.findByUserId(userId);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public AddressResponse getDefaultAddress(Long userId) {
        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ mặc định cho user ID: " + userId));
        return toDto(address);
    }

    private void resetDefaultAddress(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        for (Address addr : addresses) {
            if (addr.isDefault()) {
                addr.setDefault(false);
                addressRepository.save(addr);
            }
        }
    }

    private AddressResponse toDto(Address a) {
        return new AddressResponse(
                a.getId(),
                new AddressResponse.UserSummary(
                        a.getUser().getId(),
                        a.getUser().getEmail(),
                        a.getUser().getFullName()
                ),
                a.getFullName(),
                a.getPhone(),
                a.getProvince(),
                a.getDistrict(),
                a.getWard(),
                a.getDetail(),
                a.isDefault()
        );
    }
}
